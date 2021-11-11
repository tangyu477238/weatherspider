package cn.zifangsky.task;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.login.LoginManager;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.GupiaoCanUseManager;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.manager.LastMornManager;
import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import cn.zifangsky.repository.GupiaoKlineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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




    // 清除任务
    @Scheduled(cron = "${task.kzz.delAll}")
    public void deleteAllMyYmd() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("清除任务，Date：{0}",FORMAT.format(current)));
//        loginManager.clearStockYmd();

    }


    //MA数据 14点50进行
    @Scheduled(cron = "${task.kzz.risedown}")
    public void listMa() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("listMa，Date：{0}",FORMAT.format(current)));
        lastMornManager.listMa();

    }

    //9点35进行
    @Scheduled(cron = "${task.kzz.morn}")
    public void sellMorn() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("listMa，Date：{0}",FORMAT.format(current)));
        lastMornManager.listMa();

    }



}
