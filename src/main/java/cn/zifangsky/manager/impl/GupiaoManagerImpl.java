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
            log.info(e.getMessage());
        }
    }

    public void saveKlines(BaseGupiaoKline gupiaoKline){
        if (gupiaoKline.getPeriod().equals("5m")){
            gupiaoKline5mRepository.save((GupiaoKline5m)gupiaoKline);
            return ;
        }
        gupiaoKlineRepository.save((GupiaoKline)gupiaoKline);
    }

    @Override
    public BaseGupiaoKline getGupiaoKline(String bondId, String period, String bizDate) {
        if (period.equals("5m")){
            return gupiaoKline5mRepository.findBySymbolAndPeriodAndBizDate(bondId,period, bizDate);
        }
        return gupiaoKlineRepository.findBySymbolAndPeriodAndBizDate(bondId,period, bizDate);
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
}
