package cn.zifangsky.task;

import cn.zifangsky.common.ExecutorProcessPool;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.impl.GupiaoManagerImpl;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.model.GupiaoCanUse;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
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
public class TodayTasks {

    private final Format FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Value("${mq.kline.off}")
    private String klineOff;


    @Resource
    private DongfangManager dongfangManager;

    @Resource
    private GupiaoManagerImpl gupiaoManager;

    @Resource
    private GupiaoCanUseRepository gupiaoCanUseRepository;




    /***
     * 1分钟同步一次
     */
    @Scheduled(cron = "${task.today.schedule}")
    public void todayByFen(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行dongfeng，Date：{0}",FORMAT.format(current)));
//        dongfangManager.getToDayKline5M("399006");
    }

    /***
     * 1分钟同步一次
     */
    @Scheduled(cron = "${task.today.schedule}")
    public void todayByDay(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行dongfeng，Date：{0}",FORMAT.format(current)));
//        dongfangManager.getKline("000002");
    }



    ///////////////////////////////////////K线同步//////////////////////////////////////////////

    /***
     * 5m
     */
    @Scheduled(cron = "${task.today.kzz.5m}")
    public void todayKzzBy5m(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("todayKzzBy5m，Date：{0}",FORMAT.format(current)));
        try {
            Runnable run = new TodayTasks.TodayBuyRunnable(5);
            ExecutorProcessPool.getInstance().executeByCustomThread(run);
        }catch (Exception e){log.debug(e.toString());}
    }

    public class TodayBuyRunnable implements Runnable{
        private Integer period;
        public TodayBuyRunnable(Integer period){
            this.period = period;
        }
        @Override
        public void run(){
            List<GupiaoCanUse> list = gupiaoCanUseRepository.listSyns();
            Collections.shuffle(list);
            for (GupiaoCanUse canUse : list){
                dongfangManager.getKline(canUse.getSymbol(),period,true,true);
            }
        }
    }



    /***
     * 30m
     */
    @Scheduled(cron = "${task.today.kzz.30m}")
    public void todayKzzBy30m(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("todayKzzBy30m，Date：{0}",FORMAT.format(current)));
        try {
            Runnable run = new TodayTasks.TodayRunnable(30);
            ExecutorProcessPool.getInstance().executeByCustomThread(run);
        }catch (Exception e){log.debug(e.toString());}
    }

    /***
     * day
     */
    @Scheduled(cron = "${task.today.kzz.day}")
    public void todayKzzByDay(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("todayKzzByDay，Date：{0}",FORMAT.format(current)));
        try {
            Runnable run = new TodayTasks.TodayRunnable(101);
            ExecutorProcessPool.getInstance().executeByCustomThread(run);
        }catch (Exception e){log.debug(e.toString());}

    }


    public class TodayRunnable implements Runnable{
        private Integer period;
        public TodayRunnable(Integer period){
            this.period = period;
        }
        @Override
        public void run(){
            List<Gupiao> list = gupiaoManager.listKzz();
            Collections.shuffle(list);
            for (Gupiao gupiao : list){
                dongfangManager.getKline(gupiao.getSymbol(),period,true,true);
            }
        }
    }





    /////////////////////////////////一天一次///////////全量数据同步/////////////////////////////////////////

    /***
     * 全量可转债
     */
    @Scheduled(cron = "${task.every.gupiao.all}")
    public void gupiaoByAll(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("gupiaoByAll，Date：{0}",FORMAT.format(current)));
        dongfangManager.listKzzData();
    }



    /***
     * 日k线
     */
    @Scheduled(cron = "${task.every.kzz.day}")
    public void kzzByDay(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("todayKzzByDay，Date：{0}",FORMAT.format(current)));
        try {
            Runnable run = new TodayTasks.ToAllRunnable(101);
            ExecutorProcessPool.getInstance().executeByCustomThread(run);
        }catch (Exception e){log.debug(e.toString());}

    }

    /***
     * 5分k线
     */
    @Scheduled(cron = "${task.every.kzz.5m}")
    public void kzzBy5m(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("kzzBy5m，Date：{0}",FORMAT.format(current)));
        try {
            Runnable run = new TodayTasks.ToAllRunnable(5);
            ExecutorProcessPool.getInstance().executeByCustomThread(run);
        }catch (Exception e){log.debug(e.toString());}
    }

    /***
     * 30分k线
     */
    @Scheduled(cron = "${task.every.kzz.30m}")
    public void kzzBy30m(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("kzzBy30m，Date：{0}",FORMAT.format(current)));
        try {
            Runnable run = new TodayTasks.ToAllRunnable(30);
            ExecutorProcessPool.getInstance().executeByCustomThread(run);
        }catch (Exception e){log.debug(e.toString());}
    }



    public class ToAllRunnable implements Runnable{
        private Integer period;
        public ToAllRunnable(Integer period){
            this.period = period;
        }
        @Override
        public void run(){
            gupiaoManager.setPeriod(period);
            gupiaoManager.sysnKzzKlineAll();
        }
    }
}
