package cn.zifangsky.task;

import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.login.LoginManager;
import cn.zifangsky.manager.GupiaoCanUseManager;
import cn.zifangsky.manager.LastMornManager;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import cn.zifangsky.repository.GupiaoKlineRepository;
import com.google.common.primitives.Ints;
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
public class KzzTasks {

    private final Format FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${mq.consumer.off}")
    private String consumerOff;


    @Resource
    private GupiaoKlineRepository gupiaoKlineRepository;

    @Resource
    private GupiaoCanUseRepository gupiaoCanUseRepository;

    @Resource
    private LoginManager loginManager;

    @Resource
    private LastMornManager lastMornManager;

    @Resource
    private GupiaoCanUseManager gupiaoCanUseManager;



    /***
     * 1分钟同步一次
     *
     * 查数据
     */
    @Scheduled(cron = "${task.kzz.todayBuy}")
    public void todayBuy(){
        if ("0".equals(consumerOff)) return;
//        log.info(MessageFormat.format("开始执行todayBuy，Date：{0}",  DateTimeUtil.formatDateStr(new Date())));
//        gupiaoCanUseManager.listBuy();
    }






    /**
     * // 定时
     * @throws Exception
     */
    @Scheduled(cron = "${task.kzz.xintiao}")
    public void xintiao() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("xintiao，Date：{0}",FORMAT.format(current)));
        loginManager.queryMyStockAmount();
    }

    /***
     * // 清除任务
     * @throws Exception
     */
    @Scheduled(cron = "${task.kzz.delAll}")
    public void deleteAllMyYmd() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("清除任务，Date：{0}",FORMAT.format(current)));
        loginManager.deleteAllMyYmd(null);
    }

    /**
     * //MA数据 14点59进行
     * @throws Exception
     */
    @Scheduled(cron = "${task.kzz.risedown}")
    public void listMa() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("listMa，Date：{0}",FORMAT.format(current)));
        String startTime = "14:58:44";
        String endTime = "15:00:00";
        if (!checkRunTime(startTime, endTime)){
            return;
        }
        log.info(MessageFormat.format("进行处理，Date：{0}",FORMAT.format(current)));
        lastMornManager.listMa();

    }

    /**
     * //9点35进行
     * @throws Exception
     */
    @Scheduled(cron = "${task.kzz.morn}")
    public void sellMorn() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("listMa，Date：{0}",FORMAT.format(current)));
        String startTime = "09:34:44";
        String endTime = "09:45:00";
        if (!checkRunTime(startTime, endTime)){
            return;
        }
        log.info(MessageFormat.format("进行处理，Date：{0}",FORMAT.format(current)));
        lastMornManager.listMa();

    }

    /**
     *  判断时间区间
     * @param hours
     * @param minutes
     * @return
     */
    private boolean checkRunTime(int[] hours, int[] minutes){
        List<Integer> hourList = Ints.asList(hours);
        int cHours = DateTimeUtil.getHoursOfDay(new Date());
        List<Integer> minuteList = Ints.asList(minutes);
        int cMinute = DateTimeUtil.getMinutesOfHour(new Date());
        //判断是否在范围内
        if (hourList.contains(cHours) && minuteList.contains(cMinute)){
            return true;
        }
        return false;
    }

    /***
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    private boolean checkRunTime(String startTime, String endTime) throws Exception{
        Date start = DateTimeUtil.parseToDate(DateTimeUtil.formatDateStr(new Date(),
                "yyyy-MM-dd")+" "+startTime,"yyyy-MM-dd HH:mm:ss");
        Date end = DateTimeUtil.parseToDate(DateTimeUtil.formatDateStr(new Date(),
                "yyyy-MM-dd")+" "+endTime,"yyyy-MM-dd HH:mm:ss");
        return (DateTimeUtil.getSecondsOfTwoDate(new Date(), start)>0) && (DateTimeUtil.getSecondsOfTwoDate(new Date(), end)<0);
    }

}
