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
import cn.zifangsky.model.GupiaoKline;
import cn.zifangsky.repository.GupiaoKline5mRepository;
import cn.zifangsky.repository.GupiaoKlineRepository;
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


    @Value("${mq.consumer.etf.off}")
    private String consumerOffEtf;

    @Resource
    private GupiaoKlineRepository gupiaoKlineRepository;

    @Resource
    private DongfangManager dongfangManager;

    @Resource
    private LoginManager loginManager;



    @Scheduled(cron = "${task.cczq.zaopan}")
    public void zaopan() throws Exception{
        if ("0".equals(consumerOffEtf)) return;

        zaopan(false);

    }
    @Scheduled(cron = "${task.cczq.zaopanCheck}")
    public void zaopanCheck() throws Exception{
        if ("0".equals(consumerOffEtf)) return;

        zaopan(true);
    }


    private void zaopan(boolean flag) throws Exception{
        String stock_code = "159949";
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行zaopan，Date：{0}",FORMAT.format(current)));
        BaseGupiaoKline gupiaoKline = gupiaoKlineRepository.getKline5m("399006", 5,
                DateTimeUtil.getBeforeDay(0)+" 09:35");
        if (ComUtil.isEmpty(gupiaoKline)){
            dongfangManager.getKline("399006", 5,false);
            Thread.sleep(60000);
            gupiaoKline = gupiaoKlineRepository.getKline5m("399006", 5,
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



}
