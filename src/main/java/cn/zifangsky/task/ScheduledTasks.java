package cn.zifangsky.task;

import cn.zifangsky.manager.CrawlManager;
import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.mq.producer.CheckIPSender;
import cn.zifangsky.mq.producer.WeatherUpdateSender;
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
public class ScheduledTasks {

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

    /**
     * 天气定时更新任务
     * @author zifangsky
     * @date 2018/6/21 13:43
     * @since 1.0.0
     */
//    @Scheduled(cron = "${task.updateWeather.schedule}")
//    public void updateWeatherTask(){
//        Date current = new Date();
//        log.debug(MessageFormat.format("开始执行天气定时更新任务，Date：{0}",FORMAT.format(current)));
//
//        List<WeatherStation> list = weatherStationManager.selectAll();
//        if(list != null && list.size() > 0){
//            list.forEach(station -> {
//                weatherUpdateSender.updateWeather(weatherTopicName, station.getCode());
//            });
//        }
//    }

    /**
     * 代理IP定时检测任务（检查是否有效）
     * @author zifangsky
     * @date 2018/6/21 13:50
     * @since 1.0.0
     */
//    @Scheduled(cron = "${task.checkProxyIp.schedule}")
//    public void checkProxyIpTask(){
//        Date current = new Date();
//        log.debug(MessageFormat.format("开始执行代理IP定时检测任务，Date：{0}",FORMAT.format(current)));
//
////        1 查询数据库中所有代理IP
//        List<ProxyIp> list = proxyIpManager.selectAll();
//
//        if(list != null && list.size() > 0){
//            //2 遍历
//            list.forEach(proxyIp -> {
//                ProxyIpBO proxyIpBO = new ProxyIpBO();
//                proxyIpBO.setId(proxyIp.getId());
//                proxyIpBO.setIp(proxyIp.getIp());
//                proxyIpBO.setPort(proxyIp.getPort());
//                proxyIpBO.setType(proxyIp.getType());
//                proxyIpBO.setAddr(proxyIp.getAddr());
//                proxyIpBO.setUsed(proxyIp.getUsed());
//                proxyIpBO.setOther(proxyIp.getOther());
//                proxyIpBO.setCheckType(ProxyIpBO.CheckIPType.UPDATE);
//
//                //3 添加到队列中
//                checkIPSender.send(checkIPTopicName, proxyIpBO);
//            });
//        }
//    }







    /**
     * 代理IP定时获取任务1
     * @author zifangsky
     * @date 2018/6/21 13:53
     * @since 1.0.0
     */
    @Scheduled(cron = "${task.crawlProxyIp_1.schedule}")
    public void crawlProxyIpTask1(){
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行代理IP定时获取任务1，Date：{0}",FORMAT.format(current)));
        crawlManager.proxyIPCrawl();
    }

    /**
     * 代理IP定时获取任务2
     * @author zifangsky
     * @date 2018/6/21 13:55
     * @since 1.0.0
     */
    @Scheduled(cron = "${task.crawlProxyIp_2.schedule}")
    public void crawlProxyIpTask2(){
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行代理IP定时获取任务2，Date：{0}",FORMAT.format(current)));
        crawlManager.proxyIPCrawl2();
    }



    @Scheduled(cron = "${task.jsl.schedule}")
    public void kzzKline(){
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行kzzKline，Date：{0}",FORMAT.format(current)));
        crawlManager.getDataJsl();
    }

}
