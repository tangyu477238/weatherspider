package cn.zifangsky.task;

import cn.zifangsky.common.DateTimeUtil;
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

    @Value("${mq.topicName.checkIP}")
    private String checkIPTopicName;

    @Value("${mq.topicName.weather}")
    private String weatherTopicName;

    @Resource(name="proxyIpManager")
    private ProxyIpManager proxyIpManager;


    @Resource(name="checkIPSender")
    private CheckIPSender checkIPSender;


    @Resource(name="crawlManager")
    private CrawlManager crawlManager;

    @Value("${mq.consumer.off}")
    private String consumerOff;


    @Resource
    private DongfangManager dongfangManager;

    @Resource
    private GupiaoManager gupiaoManager;



    @Scheduled(cron = "${task.cczq.zaopan}")
    public void zaopan(){
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行dongfeng，Date：{0}",FORMAT.format(current)));
        GupiaoKline gupiaoKline = gupiaoManager.getGupiaoKline("399006", "5m", DateTimeUtil.getBeforeDay(0)+" 09:35");
        if (gupiaoKline.getClose()>gupiaoKline.getOpen() && gupiaoKline.getPercent()>0.3){ //阳线且大于0.3
            log.info("触发购买");
            log.info("触发网格交易");
        } else {
            log.info("触发清仓");
        }
    }

    @Scheduled(cron = "${task.cczq.wanpan}")
    public void wanpan(){
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行dongfeng，Date：{0}",FORMAT.format(current)));
        GupiaoKline gupiaoKline = gupiaoManager.getGupiaoKline("399006", "5m", DateTimeUtil.getBeforeDay(0)+" 09:35");
        //根据目前的账户情况，卖出或买入 建议对应的日志，根据日志统计。
        if (gupiaoKline.getClose()>gupiaoKline.getOpen() && gupiaoKline.getPercent()>0.3){ //阳线且大于0.3
            log.info("触发购买");
            log.info("触发网格交易");
        } else {
            log.info("触发清仓");
        }
    }

}
