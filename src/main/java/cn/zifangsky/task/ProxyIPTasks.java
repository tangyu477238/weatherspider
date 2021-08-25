package cn.zifangsky.task;

import cn.zifangsky.common.ExecutorProcessPool;
import cn.zifangsky.manager.CrawlManager;
import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.model.ProxyIp;
import cn.zifangsky.model.bo.ProxyIpBO;
import cn.zifangsky.mq.consumer.GupiaoCodeKlineReceiver;
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
import java.util.List;

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

    @Resource
    private ProxyIpManager proxyIpManager;

    @Resource
    private CheckIPSender checkIPSender;

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
            Runnable run = new ProxyIPTasks.ProxyRunnable(true);
            ExecutorProcessPool.getInstance().executeByCustomThread(run);
        }catch (Exception e){log.debug(e.toString());}
    }


    public class ProxyRunnable implements Runnable{
        private boolean isProxy;
        public ProxyRunnable(boolean isProxy){
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



    /**
     * 代理IP定时检测任务（检查是否有效）
     * @author zifangsky
     * @date 2018/6/21 13:50
     * @since 1.0.0
     */
    @Scheduled(cron = "${task.checkProxyIp.schedule}")
    public void checkProxyIpTask(){
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行代理IP定时检测任务，Date：{0}",FORMAT.format(current)));

        //1 查询数据库中所有代理IP
        List<ProxyIp> list = proxyIpManager.selectAll();

        if(list != null && list.size() > 0){
            //2 遍历
            list.forEach(proxyIp -> {
                ProxyIpBO proxyIpBO = new ProxyIpBO();
                proxyIpBO.setId(proxyIp.getId());
                proxyIpBO.setIp(proxyIp.getIp());
                proxyIpBO.setPort(proxyIp.getPort());
                proxyIpBO.setType(proxyIp.getType());
                proxyIpBO.setAddr(proxyIp.getAddr());
                proxyIpBO.setUsed(proxyIp.getUsed());
                proxyIpBO.setOther(proxyIp.getOther());
                proxyIpBO.setCheckType(ProxyIpBO.CheckIPType.UPDATE);

                //3 添加到队列中
                checkIPSender.send(proxyIpBO);
            });
        }
    }
}
