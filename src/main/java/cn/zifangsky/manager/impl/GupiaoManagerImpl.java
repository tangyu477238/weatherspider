package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.model.GupiaoKline;
import cn.zifangsky.model.GupiaoKline5m;
import cn.zifangsky.mq.producer.GupiaoSender;
import cn.zifangsky.repository.GupiaoKline5mRepository;
import cn.zifangsky.repository.GupiaoKlineRepository;
import cn.zifangsky.repository.GupiaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("gupiaoManagerImpl")
public class GupiaoManagerImpl implements GupiaoManager {

    @Resource
    private GupiaoKlineRepository gupiaoKlineRepository;

    @Resource
    private GupiaoKline5mRepository gupiaoKline5mRepository;

    @Resource
    private GupiaoRepository gupiaoRepository;

    @Resource
    private GupiaoSender gupiaoSender;


    /***
     * 股票k线数据存储
     * @param gupiaoKline
     */
    @Override
    public void saveKline(BaseGupiaoKline gupiaoKline) {
        try {
            BaseGupiaoKline kline = getGupiaoKline(gupiaoKline.getSymbol(), gupiaoKline.getPeriod(), gupiaoKline.getBizDate());
            if (ComUtil.isEmpty(kline)){
                saveKlines(gupiaoKline);
                return;
            }
            if (gupiaoKline.getBizDate().startsWith(DateTimeUtil.getBeforeDay(0))){ //如果是当天，请覆盖
                gupiaoKline.setId(kline.getId());
                BeanUtils.copyProperties(gupiaoKline, kline);
                saveKlines(gupiaoKline);
            }
        } catch (Exception e){
            log.debug(e.getMessage());
        }
    }

    public void saveKlines(BaseGupiaoKline gupiaoKline){
        if (gupiaoKline.getPeriod().equals("5m")){
            gupiaoKline5mRepository.save((GupiaoKline5m)gupiaoKline);
            return ;
        } else if (gupiaoKline.getPeriod().equals("day")){
            gupiaoKlineRepository.save((GupiaoKline)gupiaoKline);
        }

    }


    private List getAddGupiaoKline(List<BaseGupiaoKline> list){
        List<BaseGupiaoKline> addList = new ArrayList<>(); //新增数据
        for (BaseGupiaoKline gupiaoKline : list){
            BaseGupiaoKline kline = getGupiaoKline(gupiaoKline.getSymbol(), gupiaoKline.getPeriod(), gupiaoKline.getBizDate());
            if (ComUtil.isEmpty(kline)){
                addList.add(gupiaoKline);
                continue;
            }
        }
        return addList;
    }


    private List<BaseGupiaoKline> getTodayGupiaoKline(List<BaseGupiaoKline> list){
        List<BaseGupiaoKline> todayList = new ArrayList<>(); //当天数据
        for (BaseGupiaoKline gupiaoKline : list){
            BaseGupiaoKline kline = getGupiaoKline(gupiaoKline.getSymbol(), gupiaoKline.getPeriod(), gupiaoKline.getBizDate());
            if (ComUtil.isEmpty(kline)){
                continue;
            }
            if (gupiaoKline.getBizDate().startsWith(DateTimeUtil.getBeforeDay(0))){ //如果是当天，请覆盖
                gupiaoKline.setId(kline.getId());
                todayList.add(gupiaoKline);
            }
        }
        return todayList;
    }

    public void saveKlineAll(List<BaseGupiaoKline> list){
        if (ComUtil.isEmpty(list)){
            return;
        }
        if (list.get(0).getPeriod().equals("5m")){
            List<GupiaoKline5m> addGupiaoKline = (List<GupiaoKline5m>)(List<?>) getAddGupiaoKline(list);
            gupiaoKline5mRepository.saveAll(addGupiaoKline); //保存新增数据

            List<GupiaoKline5m> todayGupiaoKline = (List<GupiaoKline5m>)(List<?>) getTodayGupiaoKline(list);
            gupiaoKline5mRepository.saveAll(todayGupiaoKline); //覆盖当天数据
            return ;
        } else if (list.get(0).getPeriod().equals("day")){
            List<GupiaoKline> addGupiaoKline = (List<GupiaoKline>)(List<?>) getAddGupiaoKline(list);
            gupiaoKlineRepository.saveAll(addGupiaoKline); //保存新增数据

            List<GupiaoKline> todayGupiaoKline = (List<GupiaoKline>)(List<?>) getTodayGupiaoKline(list);
            gupiaoKlineRepository.saveAll(todayGupiaoKline); //覆盖当天数据
        }
    }


    @Override
    public GupiaoKline getGupiaoKline(String bondId, String period, String bizDate) {
        if (period.equals("5m")){
            return gupiaoKlineRepository.getKline5m(bondId,period, bizDate);
        }
        return gupiaoKlineRepository.getKline(bondId,period, bizDate);
    }


    /***
     * 股票数据存储
     * @param gupiao
     */
    @Override
    public void saveGupiao(Gupiao gupiao) {
        Gupiao gupiao1 = gupiaoRepository.findBySymbol(gupiao.getSymbol());
        if (gupiao1 != null){
            gupiao.setId(gupiao1.getId());
        }
        gupiaoRepository.save(gupiao);
    }

    @Override
    public void updateAllGupiaoKline() {
        List<Gupiao> list = gupiaoRepository.findAll();
        for (Gupiao gupiao : list){
            gupiaoSender.send(gupiao);
        }
    }


    @Override
    public List<Gupiao> listKzz() {
        return gupiaoRepository.getSymbolTop();
    }

    @Override
    public boolean getKlineMaxBizdate(String bondId, String period) {
        Integer sl = 0;
        if ("5m".equals(period)){
            sl = gupiaoKlineRepository.getKlineM5MaxBizdate(bondId, period);
        } else if ("day".equals(period)){
            sl = gupiaoKlineRepository.getKlineMaxBizdate(bondId, period);
        }
        if (sl==0){
            return false;
        }
        return true;
    }

}
