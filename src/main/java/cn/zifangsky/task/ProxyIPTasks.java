package cn.zifangsky.task;

import cn.zifangsky.common.ExecutorProcessPool;
import cn.zifangsky.manager.CrawlManager;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.mq.consumer.GupiaoCodeKlineReceiver;
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
public class ProxyIPTasks {

    private final Format FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource(name="crawlManager")
    private CrawlManager crawlManager;

    @Value("${mq.proxyIp.off}")
    private String proxyOff;


    /**
     * 代理IP定时获取任务1
     * @author zifangsky
     * @date 2018/6/21 13:53
     * @since 1.0.0
     */
    @Scheduled(cron = "${task.crawlProxyIp_1.schedule}")
    public void crawlProxyIpTask1(){
        if ("0".equals(proxyOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("开始执行代理IP定时获取任务1，Date：{0}",FORMAT.format(current)));
        try {
            Runnable run = new ProxyIPTasks.RunnableImpl(true);
            ExecutorProcessPool.getInstance().executeByCustomThread(run);
        }catch (Exception e){log.debug(e.toString());}
    }


    public class RunnableImpl implements Runnable{
        private boolean isProxy;
        public RunnableImpl(boolean isProxy){
            this.isProxy = isProxy;
        }
        @Override
        public void run(){
            if (isProxy){
                crawlManager.proxyIPCrawl();
                return;
            }
            crawlManager.getIPCrawl();
        }
    }


    /**
     * 代理IP定时获取任务2
     * @author zifangsky
     * @date 2018/6/21 13:55
     * @since 1.0.0
     */
    @Scheduled(cron = "${task.crawlProxyIp_2.schedule}")
    public void crawlProxyIpTask2(){
        if ("0".equals(proxyOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行代理IP定时获取任务2，Date：{0}",FORMAT.format(current)));
        crawlManager.getIPCrawl();
    }




}
