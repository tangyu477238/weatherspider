package cn.zifangsky.task;

import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.login.Login;
import cn.zifangsky.manager.CrawlManager;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.GupiaoKline;
import cn.zifangsky.mq.producer.CheckIPSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private String access_token="945e732c72cc4e519bda9655bb2e0b12";
    String stock_code = "159949";

    @Scheduled(cron = "${task.cczq.zaopan}")
    public void zaopan() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行zaopan，Date：{0}",FORMAT.format(current)));
        GupiaoKline gupiaoKline = gupiaoManager.getGupiaoKline("399006", "5m", DateTimeUtil.getBeforeDay(0)+" 09:35");
        double newPrice = Login.getNewPriceCyb(); //获取最新价格
        if (gupiaoKline.getClose()>gupiaoKline.getOpen() && gupiaoKline.getPercent()>0.3){ //阳线且大于0.3
            String original_price = String.valueOf(newPrice+0.001); //获取触发价格
            log.info("触发首次购买");
            Login.hungBuyCyb(access_token, original_price , ""+newPrice,2000);
            log.info("触发网格交易");
            Login.gridYmdCyb(access_token, newPrice+"");
        } else {
            String original_price = String.valueOf(newPrice-0.001); //获取触发价格
            log.info("触发清仓");
            Login.hungSellCyb(access_token,original_price,""+newPrice, Login.getEnableAmountCyb(access_token, stock_code));
        }
    }

    @Scheduled(cron = "${task.cczq.wanpan}")
    public void wanpan() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("wanpan，Date：{0}",FORMAT.format(current)));
        //清除条件单
        Login.deleteAllMyYmd(access_token);

//        根据实际情况补仓
        int num = Login.buchongStockNum(access_token);
        double newPrice = Login.getNewPriceCyb(); //获取最新价格
        if (num < 0){
            log.info("补充仓位");

            String original_price = String.valueOf(newPrice+0.001); //触发价格
            log.info("触发首次购买");
            Login.hungBuyCyb(access_token, original_price,""+newPrice, Math.abs(num));
        }
        if (num > 0){
            String original_price = String.valueOf(newPrice-0.001);  //触发价格

            log.info("处理多出仓位");
            Login.hungSellCyb(access_token,original_price,""+newPrice, num);
        }


    }

    @Scheduled(cron = "${task.cczq.xintiao}")
    public void xintiao() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("wanpan，Date：{0}",FORMAT.format(current)));
//      心跳线程
        Login.queryMyStockAmount(access_token);
    }

}
