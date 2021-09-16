package cn.zifangsky.task;

import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.enums.KlineEnum;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.impl.GupiaoManagerImpl;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.mq.producer.GupiaoCodeKlineSender;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import cn.zifangsky.repository.GupiaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
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




    @Value("${mq.kline.off}")
    private String klineOff;

    @Value("${mq.kline.today.off}")
    private String klineTodayOff;

    @Resource
    private DongfangManager dongfangManager;

    @Resource
    private GupiaoManagerImpl gupiaoManager;

    @Resource
    private GupiaoCanUseRepository gupiaoCanUseRepository;

    @Resource
    private GupiaoRepository gupiaoRepository;

    @Resource
    private GupiaoCodeKlineSender gupiaoCodeKlineSender;


    /***
     * 1分钟同步一次
     */
    @Scheduled(cron = "${task.today.schedule}")
    public void todayByFen(){
        if ("0".equals(klineTodayOff)) return;
        log.debug(MessageFormat.format("开始执行dongfeng，Date：{0}",DateTimeUtil.formatTimetoString(new Date())));
//        dongfangManager.getToDayKline5M("399006");
    }

    /***
     * 1分钟同步一次
     */
    @Scheduled(cron = "${task.today.schedule}")
    public void todayByDay(){
        if ("0".equals(klineTodayOff)) return;
        log.debug(MessageFormat.format("开始执行dongfeng，Date：{0}", DateTimeUtil.formatTimetoString(new Date())));
//        dongfangManager.getKline("000002");
    }



    /////////////////////////////////当天//////K线同步//////////////////////////////////////////////


//    /***
//     * 5m(按信号同步)
//     */
//    @Scheduled(cron = "${task.today.kzz.5m}")
//    public void todayKzzBy5m(){
//        if ("0".equals(klineTodayOff)) return;
//        log.info(MessageFormat.format("todayKzzBy5m，Date：{0}",DateTimeUtil.formatTimetoString(new Date())));
//        List<String> listM5 = gupiaoCanUseRepository.listSyns();
//        Integer period = KlineEnum.K_5M.getId();
//        Gupiao gupiao;
//        for (String symbol : listM5){
//            gupiao = new Gupiao();
//            gupiao.setSymbol(symbol);
//            gupiao.setPeriod(period);
//            gupiao.setFollowers(1);
//            gupiaoCodeKlineSender.send(gupiao);
//        }
//
//    }


    /***
     * 5m
     */
    @Scheduled(cron = "${task.today.kzz.5m}")
    public void todayKzzBy5m(){
        if ("0".equals(klineTodayOff)) return;
        log.info(MessageFormat.format("todayKzzBy5m，Date：{0}",DateTimeUtil.formatTimetoString(new Date())));
        TodayByKline(KlineEnum.K_5M.getId());

    }



    /***
     * 30m
     */
    @Scheduled(cron = "${task.today.kzz.30m}")
    public void todayKzzBy30m(){
        if ("0".equals(klineTodayOff)) return;
        log.info(MessageFormat.format("--------todayKzzBy30m，Date：{0}-------------",DateTimeUtil.formatTimetoString(new Date())));
        TodayByKline(KlineEnum.K_30M.getId());
    }



    /***
     * 仅仅同步数据
     * 当天数据同步线程
     */
    public void TodayByKline(Integer period){
        try {
            List<Gupiao> list = gupiaoManager.listBeforeTime(period);
            for (Gupiao gupiao : list){
                gupiao.setPeriod(period);
                gupiao.setFollowers(1); //当天
                gupiaoCodeKlineSender.send(gupiao);
            }
        }catch (Exception e){log.debug(e.toString());}
    }










    /////////////////////////////////一天一次///////////全量数据同步/////////////////////////////////////////

    /***
     * 全量 名称列表
     */
    @Scheduled(cron = "${task.every.gupiao.all}")
    public void gupiaoByAll(){
        if ("0".equals(klineOff)) return;
        log.info(MessageFormat.format("gupiaoByAll，Date：{0}",DateTimeUtil.formatTimetoString(new Date())));
        gupiaoRepository.delKzzAll();
        dongfangManager.listKzzData();
    }



    /***
     * 全量 日k线
     */
    @Scheduled(cron = "${task.every.kzz.day}")
    public void kzzByDay(){
        if ("0".equals(klineOff)) return;
        log.info(MessageFormat.format("todayKzzByDay，Date：{0}",DateTimeUtil.formatTimetoString(new Date())));
        gupiaoManager.sysnKzzKlineAll(KlineEnum.K_1D.getId());

    }

    /***
     * 全量 5分k线
     */
    @Scheduled(cron = "${task.every.kzz.5m}")
    public void kzzBy5m(){
        if ("0".equals(klineOff)) return;
        
        log.info(MessageFormat.format("kzzBy5m，Date：{0}",DateTimeUtil.formatTimetoString(new Date())));

        gupiaoManager.sysnKzzKlineAll(KlineEnum.K_5M.getId());

    }

    /***
     * 全量 30分k线
     */
    @Scheduled(cron = "${task.every.kzz.30m}")
    public void kzzBy30m(){
        if ("0".equals(klineOff)) return;
        log.info(MessageFormat.format("----------------kzzBy30m，Date：{0}---------------------",DateTimeUtil.formatTimetoString(new Date())));
        gupiaoManager.sysnKzzKlineAll(KlineEnum.K_30M.getId());

    }


}
