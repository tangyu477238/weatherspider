package cn.zifangsky.task;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.login.LoginManager;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.repository.GupiaoKline5mRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
public class KzzTasks {

    private final Format FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${mq.consumer.off}")
    private String consumerOff;

    @Value("${mq.consumer.etf.off}")
    private String consumerOffEtf;

    @Resource
    private GupiaoKline5mRepository gupiaoKline5mRepository;

    @Resource
    private DongfangManager dongfangManager;

    @Resource
    private LoginManager loginManager;

    @Resource
    private GupiaoManager gupiaoManager;

    @Scheduled(cron = "${task.cczq.zaopan}")
    public void zaopan() throws Exception{
        if ("0".equals(consumerOffEtf)) return;

        zaopan(false);

    }



    private void zaopan(boolean flag) throws Exception{

        List<Gupiao> list = gupiaoManager.listKzz();
        for (Gupiao gupiao : list){

        }


        String stock_code = "159949";
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行zaopan，Date：{0}",FORMAT.format(current)));
        BaseGupiaoKline gupiaoKline = gupiaoKline5mRepository.findBySymbolAndPeriodAndBizDate("399006", "5m",
                DateTimeUtil.getBeforeDay(0)+" 09:35");
        if (ComUtil.isEmpty(gupiaoKline)){
            dongfangManager.getKline("399006", "5",false);
            Thread.sleep(60000);
            gupiaoKline = gupiaoKline5mRepository.findBySymbolAndPeriodAndBizDate("399006", "5m",
                    DateTimeUtil.getBeforeDay(0)+" 09:35");
        }
        double newPrice = loginManager.getNewPriceCyb(); //获取最新价格

        if (gupiaoKline.getClose()<gupiaoKline.getOpen() || gupiaoKline.getPercent()<0.3) { //阳线且大于0.3
            String original_price = String.valueOf(newPrice-0.001); //获取触发价格
            log.debug("触发清仓");
            loginManager.hungSellCyb(original_price,""+newPrice,
                    loginManager.getEnableAmount(stock_code));
            return;
        }

        if (flag){ //检查任务开启
            return;
        }

        if (gupiaoKline.getClose()>gupiaoKline.getOpen() && gupiaoKline.getPercent()>0.3){ //阳线且大于0.3

            String original_price = String.valueOf(newPrice+0.001); //获取触发价格
            log.debug("触发首次购买");
            loginManager.hungBuyCyb(original_price , ""+newPrice,2000);
            log.debug("触发网格交易");
            loginManager.gridYmdCyb(newPrice+"");
            return;
        }


    }


    @Scheduled(cron = "${task.cczq.wanpan}")
    public void wanpan() throws Exception{
        if ("0".equals(consumerOffEtf)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("wanpan，Date：{0}",FORMAT.format(current)));
        log.debug("清除条件单");
        loginManager.deleteAllMyYmd();

//        根据实际情况补仓
        int num = loginManager.buchongStockNum();
        double newPrice = loginManager.getNewPriceCyb(); //获取最新价格
        if (num < 0){
            log.debug("补充仓位");

            String original_price = String.valueOf(newPrice+0.001); //触发价格
            log.debug("触发首次购买");
            loginManager.hungBuyCyb(original_price,""+newPrice, Math.abs(num));
        }
        if (num > 0){
            String original_price = String.valueOf(newPrice-0.001);  //触发价格

            log.debug("处理多出仓位");
            loginManager.hungSellCyb(original_price,""+newPrice, num);
        }


    }

    @Scheduled(cron = "${task.cczq.xintiao}")
    public void xintiao() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("xintiao，Date：{0}",FORMAT.format(current)));
//      心跳线程
        loginManager.deleteAllMyYmd();
    }


    @Scheduled(cron = "${task.cczq.risedown}")
    public void taskYmd() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("taskYmd，Date：{0}",FORMAT.format(current)));

        String listJson = loginManager.queryMyStockAmount (); //数据列表
        JSONObject jsonObj = JSONUtil.parseObj(listJson);
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
                addYmd(map, stock_code, stock_name, enable_amount);
            }
            if (enable_amount==0){
                checkAddYmd(map, stock_code, -1 , "34");
                checkAddYmd(map, stock_code, -1 , "35");
                checkAddYmd(map, stock_code, -1 , "7");
            }
        }
    }



    private void addRisedownSell(Map map,String stock_code, String stock_name, Integer enable_amount, String newPrice) throws Exception{
        if (checkAddYmd(map, stock_code, enable_amount,"7")){
            return;
        }
        String risedown_rate = "2.0";
        double nPrice = Double.parseDouble(newPrice);
        String original_price = String.valueOf(nPrice+0.001);
        loginManager.risedownSell(stock_code, stock_name, original_price, risedown_rate, newPrice, enable_amount); //添加回落单
     }

    private void addStopProfitAndLoss(Map map,String stock_code, String stock_name, Integer enable_amount, String newPrice) throws Exception{
        if (checkAddYmd(map, stock_code, enable_amount,"35")){
            return;
        }

        String stop_loss_rate = "2.0"; //止

        String stop_profit_rate = "100";
        String stop_profit_price = getProfitPrice(newPrice, Double.parseDouble(stop_profit_rate));
        String stop_loss_price = getLossPrice(newPrice, Double.parseDouble(stop_loss_rate));
        loginManager.stopProfitAndLoss(stock_code,stock_name,
                newPrice,stop_profit_rate, stop_profit_price, stop_loss_rate, stop_loss_price, newPrice, enable_amount);

    }

    private void addHungSell(Map map,String stock_code, String stock_name, Integer enable_amount, String newPrice) throws Exception{
        if (checkAddYmd(map, stock_code, enable_amount,"34")){
            return;
        }

        String stop_loss_rate = "1"; //止
        String original_price = getLossPrice(newPrice, Double.parseDouble(stop_loss_rate));
        loginManager.hungSell(stock_code,stock_name, original_price, newPrice, enable_amount);
    }


    private void addYmd(Map map,String stock_code, String stock_name, Integer enable_amount) throws Exception{
        String newPrice = loginManager.getNewPrice(stock_code); //获取最新
        log.debug("-------------taskYmd------"+stock_code+"----"+enable_amount +"--" + newPrice);

        addRisedownSell(map, stock_code, stock_name, enable_amount, newPrice);

        addStopProfitAndLoss(map, stock_code, stock_name, enable_amount, newPrice);

        addHungSell(map, stock_code, stock_name, enable_amount, newPrice);



    }

    public String getLossPrice(String newPrice, double rate) {
        double nPrice = Double.parseDouble(newPrice);
        BigDecimal lossPrice = new BigDecimal(nPrice-(rate*nPrice/100));
        return String.valueOf(lossPrice.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
    }
    public String getProfitPrice(String newPrice, double rate) {
        double nPrice = Double.parseDouble(newPrice);
        BigDecimal profitPrice = new BigDecimal(nPrice+(rate*nPrice/100));
        return String.valueOf(profitPrice.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
     * 检查是否存在或不等
     * @param map
     * @param stock_code
     * @param enable_amount
     * @param strategy_id
     * @return
     * @throws Exception
     */
    public boolean checkAddYmd(Map map, String stock_code, Integer enable_amount, String strategy_id) throws Exception{
        if (map.containsKey(stock_code+strategy_id)){
            String arr[] = map.get(stock_code+strategy_id).toString().split("_");
            if (arr[0].equals(String.valueOf(enable_amount))){
                return true;
            }
            loginManager.deleteYmd(arr[1]); //删除原条件单
        }
        return false;
    }
}
