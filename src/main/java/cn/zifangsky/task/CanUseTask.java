package cn.zifangsky.task;

import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.GupiaoCanUseManager;
import cn.zifangsky.manager.impl.GupiaoManagerImpl;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;

@Component
@Slf4j
public class CanUseTask {
    @Value("${mq.kline.off}")
    private String klineOff;

    @Resource
    private GupiaoCanUseManager gupiaoCanUseManager;




    /***
     * 1分钟同步一次
     */
    @Scheduled(cron = "${task.today.canUse}")
    public void todayCanUse(){
        if ("0".equals(klineOff)) return;
        log.info(MessageFormat.format("开始执行todayCanUse，Date：{0}",  DateTimeUtil.formatDateTimetoString(new Date())));
        gupiaoCanUseManager.addCanUse();
    }

}
