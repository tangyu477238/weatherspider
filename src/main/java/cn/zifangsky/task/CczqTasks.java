package cn.zifangsky.task;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.login.LoginManager;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.model.GupiaoKline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 定时任务配置
 *
 * @author zifangsky
 * @date 2018/6/21
 * @since 1.0.0
 */
@Component
@Slf4j
public class CczqTasks {

    private final Format FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${mq.consumer.off}")
    private String consumerOff;

    @Resource
    private GupiaoManager gupiaoManager;

    @Resource
    private DongfangManager dongfangManager;

    @Resource
    private LoginManager loginManager;

    String stock_code = "159949";

    @Scheduled(cron = "${task.cczq.zaopan}")
    public void zaopan() throws Exception{
        if ("0".equals(consumerOff)) return;

        zaopan(false);

    }
    @Scheduled(cron = "${task.cczq.zaopanCheck}")
    public void zaopanCheck() throws Exception{
        if ("0".equals(consumerOff)) return;

        zaopan(true);
    }


    private void zaopan(boolean flag) throws Exception{
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行zaopan，Date：{0}",FORMAT.format(current)));
        GupiaoKline gupiaoKline = gupiaoManager.getGupiaoKline("399006", "5m",
                DateTimeUtil.getBeforeDay(0)+" 09:35");
        if (ComUtil.isEmpty(gupiaoKline)){
            dongfangManager.getKline("399006", "5",false);
            Thread.sleep(60000);
            gupiaoKline = gupiaoManager.getGupiaoKline("399006", "5m",
                    DateTimeUtil.getBeforeDay(0)+" 09:35");
        }
        double newPrice = loginManager.getNewPriceCyb(); //获取最新价格

        if (gupiaoKline.getClose()<gupiaoKline.getOpen() || gupiaoKline.getPercent()<0.3) { //阳线且大于0.3
            String original_price = String.valueOf(newPrice-0.001); //获取触发价格
            log.info("触发清仓");
            loginManager.hungSellCyb(original_price,""+newPrice,
                    loginManager.getEnableAmount(stock_code));
            return;
        }

        if (flag){ //检查任务开启
            return;
        }

        if (gupiaoKline.getClose()>gupiaoKline.getOpen() && gupiaoKline.getPercent()>0.3){ //阳线且大于0.3

            String original_price = String.valueOf(newPrice+0.001); //获取触发价格
            log.info("触发首次购买");
            loginManager.hungBuyCyb(original_price , ""+newPrice,2000);
            log.info("触发网格交易");
            loginManager.gridYmdCyb(newPrice+"");
            return;
        }


    }


    @Scheduled(cron = "${task.cczq.wanpan}")
    public void wanpan() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("wanpan，Date：{0}",FORMAT.format(current)));
        log.info("清除条件单");
        loginManager.deleteAllMyYmd();

//        根据实际情况补仓
        int num = loginManager.buchongStockNum();
        double newPrice = loginManager.getNewPriceCyb(); //获取最新价格
        if (num < 0){
            log.info("补充仓位");

            String original_price = String.valueOf(newPrice+0.001); //触发价格
            log.info("触发首次购买");
            loginManager.hungBuyCyb(original_price,""+newPrice, Math.abs(num));
        }
        if (num > 0){
            String original_price = String.valueOf(newPrice-0.001);  //触发价格

            log.info("处理多出仓位");
            loginManager.hungSellCyb(original_price,""+newPrice, num);
        }


    }

    @Scheduled(cron = "${task.cczq.xintiao}")
    public void xintiao() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("xintiao，Date：{0}",FORMAT.format(current)));
//      心跳线程
        loginManager.queryMyStockAmount();
    }


    @Scheduled(cron = "${task.cczq.risedown}")
    public void taskRisedownYmd() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("taskRisedownYmd，Date：{0}",FORMAT.format(current)));

        String json = loginManager.queryMyStockAmount ();
        JSONObject jsonObj = JSONUtil.parseObj(json);
        if (jsonObj.getInt("total") == 0){
            return ;
        }
        Map map = loginManager.listMyYmd(); //获取条件列表
        JSONArray jsonArray = jsonObj.getJSONArray("data");
        for (Object object : jsonArray){
            JSONObject jsonObject = (JSONObject)object;
            Integer enable_amount = jsonObject.getInt("enable_amount");
            String stock_code = jsonObject.getStr("stock_code");
            String stock_name = jsonObject.getStr("stock_name");
            if (enable_amount>0 && (stock_code.startsWith("11")||stock_code.startsWith("12"))){
                double newPrice = Double.parseDouble(loginManager.getNewPrice(stock_code)); //获取最新
                log.info("-------------taskYmd------"+stock_code+"----"+enable_amount);

                if (!map.containsKey(stock_code+"7")){ //落
                    String original_price = String.valueOf(newPrice+0.001);
                    loginManager.risedownSell(stock_code, stock_name, original_price, "1.5", String.valueOf(newPrice), enable_amount); //添加回落单
                }

                if (!map.containsKey(stock_code+"35")){ //止
                    String original_price = String.valueOf(newPrice+0.001);
                    loginManager.stopProfitAndLoss(stock_code,stock_name,
                            String.valueOf(newPrice),"1","","1","", String.valueOf(newPrice), enable_amount);
                }

                if (!map.containsKey(stock_code+"34")){ //定
                    String original_price = String.valueOf(newPrice+0.001);
                    loginManager.hungSell(stock_code,stock_name, original_price, String.valueOf(newPrice), enable_amount);
                }
            }
        }
    }

}
