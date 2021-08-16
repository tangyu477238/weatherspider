package cn.zifangsky.task;

import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.manager.impl.GupiaoManagerImpl;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.mq.producer.GupiaoCodeKlineSender;
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
public class TodayTasks {

    private final Format FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Value("${mq.kline.off}")
    private String klineOff;


    @Resource
    private DongfangManager dongfangManager;

    @Resource
    private GupiaoManagerImpl gupiaoManager;




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
        List<Gupiao> list = gupiaoManager.listKzz();
        for (Gupiao gupiao : list){
            dongfangManager.getKline(gupiao.getSymbol(),"5",true,true);
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
        List<Gupiao> list = gupiaoManager.listKzz();
        for (Gupiao gupiao : list){
            dongfangManager.getKline(gupiao.getSymbol(),"30",true,true);
        }
    }

    /***
     * day
     */
    @Scheduled(cron = "${task.today.kzz.day}")
    public void todayKzzByDay(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.debug(MessageFormat.format("todayKzzByDay，Date：{0}",FORMAT.format(current)));
        List<Gupiao> list = gupiaoManager.listKzz();
        for (Gupiao gupiao : list){
            dongfangManager.getKline(gupiao.getSymbol(),"101",true,true);
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
        gupiaoManager.setPeriod(101);
        gupiaoManager.sysnKzzKlineAll();

    }

    /***
     * 5分k线
     */
    @Scheduled(cron = "${task.every.kzz.5m}")
    public void kzzBy5m(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("kzzBy5m，Date：{0}",FORMAT.format(current)));
        gupiaoManager.setPeriod(5);
        gupiaoManager.sysnKzzKlineAll();
    }

    /***
     * 30分k线
     */
    @Scheduled(cron = "${task.every.kzz.30m}")
    public void kzzBy30m(){
        if ("0".equals(klineOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("kzzBy30m，Date：{0}",FORMAT.format(current)));
        gupiaoManager.setPeriod(30);
        gupiaoManager.sysnKzzKlineAll();
    }

}
