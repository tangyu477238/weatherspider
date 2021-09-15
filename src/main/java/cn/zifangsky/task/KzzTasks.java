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
import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.model.Gupiao;
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
    private DongfangManager dongfangManager;

    @Resource
    private LoginManager loginManager;

    @Resource
    private GupiaoManager gupiaoManager;

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
        log.info(MessageFormat.format("开始执行todayBuy，Date：{0}",  DateTimeUtil.formatDateStr(new Date())));
        gupiaoCanUseManager.listBuy();
    }




    // 清除任务
    @Scheduled(cron = "${task.kzz.delAll}")
    public void deleteAllMyYmd() throws Exception{
        if ("0".equals(consumerOff)) return;
        Date current = new Date();
        log.info(MessageFormat.format("xintiao，Date：{0}",FORMAT.format(current)));
//      心跳线程
        loginManager.clearStockYmd();


    }

//
//
//    @Scheduled(cron = "${task.kzz.risedown}")
//    public void taskYmd() throws Exception{
//        if ("0".equals(consumerOff)) return;
//        Date current = new Date();
//        log.debug(MessageFormat.format("taskYmd，Date：{0}",FORMAT.format(current)));
//
//        String listJson = loginManager.queryMyStockAmount (); //数据列表
//        JSONObject jsonObj = JSONUtil.parseObj(listJson);
//        if (jsonObj.getInt("total") == 0){
//            return ;
//        }
//        Map map = loginManager.listMyYmd(); //获取条件列表
//        JSONArray jsonArray = jsonObj.getJSONArray("data");
//        for (Object object : jsonArray){
//            JSONObject jsonObject = (JSONObject)object;
//            Integer enable_amount = jsonObject.getInt("enable_amount");
//            String stock_code = jsonObject.getStr("stock_code");
//            String stock_name = jsonObject.getStr("stock_name");
//            if (enable_amount>0 && (stock_code.startsWith("11")||stock_code.startsWith("12"))){
//                loginManager.addYmd(map, stock_code, stock_name, enable_amount);
//            }
//            if (enable_amount==0){
//                loginManager.checkAddYmd(map, stock_code, -1 , "34");
//                loginManager.checkAddYmd(map, stock_code, -1 , "35");
//                loginManager.checkAddYmd(map, stock_code, -1 , "7");
//            }
//        }
//    }
}
