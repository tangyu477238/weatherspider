package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.model.GupiaoKline;
import cn.zifangsky.mq.producer.GupiaoSender;
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
    private GupiaoRepository gupiaoRepository;

    @Resource
    private GupiaoSender gupiaoSender;


    /***
     * 股票k线数据存储
     * @param gupiaoKline
     */
    @Override
    public void saveKline(GupiaoKline gupiaoKline) {
        try {
            GupiaoKline kline = getGupiaoKline(gupiaoKline.getSymbol(), gupiaoKline.getPeriod(), gupiaoKline.getBizDate());
            if (ComUtil.isEmpty(kline)){
                gupiaoKlineRepository.save(gupiaoKline);
                return;
            }
            if (gupiaoKline.getBizDate().startsWith(DateTimeUtil.getBeforeDay(0))){ //如果是当天，请覆盖
                gupiaoKline.setId(kline.getId());
                BeanUtils.copyProperties(gupiaoKline, kline);
                gupiaoKlineRepository.save(kline);
            }
        } catch (Exception e){
            log.info(e.getMessage());
        }
    }

    @Override
    public GupiaoKline getGupiaoKline(String bondId, String period, String bizDate) {

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
}
