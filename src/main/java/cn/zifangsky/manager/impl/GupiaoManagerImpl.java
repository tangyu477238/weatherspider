package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.model.GupiaoKline;
import cn.zifangsky.repository.GupiaoKlineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service("gupiaoManagerImpl")
public class GupiaoManagerImpl implements GupiaoManager {

    @Resource
    private GupiaoKlineRepository gupiaoKlineRepository;



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
            }
        } catch (Exception e){
            log.info(e.getMessage());
        }
    }

    @Override
    public GupiaoKline getGupiaoKline(String bondId, String period, String bizDate) {

        return gupiaoKlineRepository.findBySymbolAndPeriodAndBizDate(bondId,period, bizDate);
    }
}
