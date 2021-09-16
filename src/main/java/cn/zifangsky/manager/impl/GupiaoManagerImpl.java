package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.enums.KlineEnum;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.model.*;
import cn.zifangsky.mq.producer.GupiaoCodeKlineSender;
import cn.zifangsky.repository.GupiaoKline30mRepository;
import cn.zifangsky.repository.GupiaoKline5mRepository;
import cn.zifangsky.repository.GupiaoKlineRepository;
import cn.zifangsky.repository.GupiaoRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("gupiaoManager")
@Data
public class GupiaoManagerImpl implements GupiaoManager {

    @Resource
    private GupiaoRepository gupiaoRepository;

    @Resource
    private GupiaoCodeKlineSender gupiaoCodeKlineSender; //获取k线列表


    @Resource
    private GupiaoKlineRepository gupiaoKlineRepository; //获取day k线对象

    @Resource
    private GupiaoKline5mRepository gupiaoKline5mRepository; //获取5k线对象

    @Resource
    private GupiaoKline30mRepository gupiaoKline30mRepository; //获取30k线对象

    @Override
    public void updateTime(String bondId) {
        Gupiao gupiao = gupiaoRepository.findBySymbol(bondId);
        gupiao.setTotal_shares(System.currentTimeMillis());
        gupiaoRepository.save(gupiao);
    }


//    /***
//     * 股票k线数据存储
//     * @param gupiaoKline
//     */
//    @Override
//    public void saveKline(BaseGupiaoKline gupiaoKline) {
//        try {
//            BaseGupiaoKline kline = getGupiaoKline(gupiaoKline.getSymbol(), gupiaoKline.getBizDate());
//            if (ComUtil.isEmpty(kline)){
//                saveKlines(gupiaoKline);
//                return;
//            }
//            if (gupiaoKline.getBizDate().startsWith(DateTimeUtil.getBeforeDay(0))){ //如果是当天，请覆盖
//                gupiaoKline.setId(kline.getId());
//                BeanUtils.copyProperties(gupiaoKline, kline);
//                saveKlines(gupiaoKline);
//            }
//        } catch (Exception e){
//            log.debug(e.getMessage());
//        }
//    }

//    public void saveKlines(BaseGupiaoKline gupiaoKline){
//        if (gupiaoKline.getPeriod().equals("5m")){
//            gupiaoKline5mRepository.save((GupiaoKline5m)gupiaoKline);
//            return ;
//        } else if (gupiaoKline.getPeriod().equals("day")){
//            gupiaoKlineRepository.save((GupiaoKline)gupiaoKline);
//        }
//
//    }


    private List getAddGupiaoKline(List<BaseGupiaoKline> list){
        List<String> listDate = listKlineBizDate(list.get(0).getSymbol(), list.get(0).getPeriod());
        List<BaseGupiaoKline> addList = list.stream()
                .filter(x -> !listDate.contains(x.getBizDate())
                        && x.getBizDate().compareTo(DateTimeUtil.getPeriodDate(x.getPeriod())) <= 0)
                .collect(Collectors.toList());
        return addList;
    }


    private List<BaseGupiaoKline> getTodayGupiaoKline(List<BaseGupiaoKline> list){
        List<BaseGupiaoKline> todayList = new ArrayList<>(); //当天数据
        for (BaseGupiaoKline gupiaoKline : list){
            BaseGupiaoKline kline = getGupiaoKline(gupiaoKline.getSymbol(), gupiaoKline.getBizDate(), gupiaoKline.getPeriod());
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

    @Override
    public void saveKlineAll(List<BaseGupiaoKline> list){
        if (ComUtil.isEmpty(list)){
            return;
        }
        List klineList = getAddGupiaoKline(list); //获取有用数据
        addGupiaoKlineAll(klineList);  //保存数据
    }

    private void addGupiaoKlineAll(List<BaseGupiaoKline> listBase) {
        if (ComUtil.isEmpty(listBase)){
            return;
        }
        if (listBase.get(0).getPeriod()==KlineEnum.K_5M.getId()){
            List<GupiaoKline5m> list = listBase.stream()
                    .map((item) -> {
                        GupiaoKline5m gupiaoKline5m = new GupiaoKline5m();
                        BeanUtils.copyProperties(item, gupiaoKline5m);
                        return gupiaoKline5m;
                    }).collect(Collectors.toList());
            gupiaoKline5mRepository.saveAll(list); //保存新增数据
        } else if (listBase.get(0).getPeriod()==KlineEnum.K_30M.getId()){
//            GupiaoKline30m gupiaoKline30m;
//            List<GupiaoKline30m> list = new ArrayList<>();
//            for (BaseGupiaoKline kline : listBase){
//                gupiaoKline30m = new GupiaoKline30m();
//                BeanUtils.copyProperties(kline, gupiaoKline30m);
//
//                String biz_date = DateTimeUtil.getPeriodDate(Double.valueOf(listBase.get(0).getPeriod()));
//                if (gupiaoKline30m.getBizDate().compareTo(biz_date) <= 0){ //控制
//                    list.add(gupiaoKline30m);
//                }
//            }
            List<GupiaoKline30m> list = listBase.stream()
                .map((item) -> {
                    GupiaoKline30m gupiaoKline30m = new GupiaoKline30m();
                    BeanUtils.copyProperties(item, gupiaoKline30m);
                    return gupiaoKline30m;
                }).collect(Collectors.toList());
            gupiaoKline30mRepository.saveAll(list); //保存新增数据
        } else if (listBase.get(0).getPeriod()==KlineEnum.K_1D.getId()){
            List<GupiaoKline> list = listBase.stream()
                .map((item) -> {
                    GupiaoKline gupiaoKline = new GupiaoKline();
                    BeanUtils.copyProperties(item, gupiaoKline);
                    return gupiaoKline;
                }).collect(Collectors.toList());
            gupiaoKlineRepository.saveAll(list); //保存新增数据
        }

    }

    public List<String> listKlineBizDate(String bondId, Integer period) {
        if (period== KlineEnum.K_5M.getId()){
            return gupiaoKlineRepository.listKlineBizDate5m(bondId, period);
        } else if (period==KlineEnum.K_30M.getId()){
            return gupiaoKlineRepository.listKlineBizDate30m(bondId, period);
        } else if (period==KlineEnum.K_1D.getId()){
            return gupiaoKlineRepository.listKlineBizDate(bondId, period);
        }
        return null;
    }

    @Override
    public GupiaoKline getGupiaoKline(String bondId, String bizDate, Integer period) {
        if (period== KlineEnum.K_5M.getId()){
            return gupiaoKlineRepository.getKline5m(bondId, period, bizDate);
        } else if (period==KlineEnum.K_30M.getId()){
            return gupiaoKlineRepository.getKline30m(bondId, period, bizDate);
        } else if (period==KlineEnum.K_1D.getId()){
            return gupiaoKlineRepository.getKline(bondId, period, bizDate);
        }
        return null;
    }


//    /***
//     * 股票数据存储
//     * @param gupiao
//     */
//    @Override
//    public void saveGupiao(Gupiao gupiao) {
//        Gupiao gupiao1 = gupiaoRepository.findBySymbol(gupiao.getSymbol());
//        if (gupiao1 != null){
//            gupiao.setId(gupiao1.getId());
//        }
//        gupiaoRepository.save(gupiao);
//    }

//    @Override
//    public void updateAllGupiaoKline() {
//        List<Gupiao> list = gupiaoRepository.findAll();
//        for (Gupiao gupiao : list){
//            gupiaoSender.send(gupiao);
//        }
//    }


    @Override
    public List<Gupiao> listKzz() {
        return listKzzKline(null);
    }

    public List<Gupiao> listKzzKline(Integer period) {
        if (period==KlineEnum.K_5M.getId()){
            return gupiaoRepository.listkzz5M();
        } else if (period==KlineEnum.K_30M.getId()){
            return gupiaoRepository.listkzz30M();
        } else if (period==KlineEnum.K_1D.getId()){
            return gupiaoRepository.listkzz1Day();
        }
        return gupiaoRepository.listkzz();
    }

    @Override
    public boolean getKlineMaxBizdate(String bondId, Integer period) {
        Integer sl = 0;
        if (period==KlineEnum.K_5M.getId()){
            sl = gupiaoKlineRepository.getKline5mMaxBizdate(bondId);
        } else if (period==KlineEnum.K_30M.getId()){
            sl = gupiaoKlineRepository.getKline30mMaxBizdate(bondId);
        } else if (period==KlineEnum.K_1D.getId()){
            sl = gupiaoKlineRepository.getKlineMaxBizdate(bondId);
        }
        if (sl==0){
            return false;
        }
        return true;
    }

    @Override
    public void sysnKzzKlineAll(Integer period) {
        List<Gupiao> list = listKzzKline(period);
        for (Gupiao gupiao : list){
            gupiao.setPeriod(period);
            if (getKlineMaxBizdate(gupiao.getSymbol(), gupiao.getPeriod())){
                continue;
            }
            gupiaoCodeKlineSender.send(gupiao);
        }
    }



    @Override
    public List<Gupiao> listBeforeTime(Integer period) {
        if (period==KlineEnum.K_30M.getId()){
            return gupiaoRepository.listBeforeTime30m(period);
        } else  if (period==KlineEnum.K_5M.getId()){
            return gupiaoRepository.listBeforeTime5m(period);
        }
        return null;
    }

    @Override
    public Gupiao getBeforeTime(String symbol, Integer period) {
        if (period==KlineEnum.K_30M.getId()){
            return gupiaoRepository.getBeforeTime30m(symbol, period);
        } else if (period==KlineEnum.K_5M.getId()){
            return gupiaoRepository.getBeforeTime5m(symbol, period);
        }
        return null;
    }
}
