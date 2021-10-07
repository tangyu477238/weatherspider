package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.common.HttpMethodUtil;
import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.repository.GupiaoRepository;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@Slf4j
public class DongfangServiceImpl {
    @Resource
    private GupiaoManagerImpl gupiaoManager;
    @Resource
    private GupiaoRepository gupiaoRepository;

    public void getKine(String bondId, Integer period){

        try {
            Integer num = HttpMethodUtil.getBizdate(period);
            String beg = gupiaoRepository.getBizDate(num).replace("-","");
            beg = DateTimeUtil.formatDateStr(DateTimeUtil.addDays(new Date(),-1000),"yyyyMMdd");
            String klineUrl = HttpMethodUtil.getUrl(bondId, period, beg);
            String result = HttpMethodUtil.doGet(klineUrl);
            if (ComUtil.isEmpty(result)){
                return;
            }
            log.info(period+"------------"+bondId);
            result = result.split("\\(")[1].split("\\)")[0];
            JSONObject object = JSONObject.parseObject(result).getJSONObject("data");
            String symbol = object.getString("code");
            JSONArray jsonArray = object.getJSONArray("klines");
            List<BaseGupiaoKline> list = new ArrayList<>();
            for (int i = 0; jsonArray != null && i < jsonArray.size(); i++) {
                try {
                    String jsonArray1[] = jsonArray.get(i).toString().split(",");
                    BaseGupiaoKline kzz1 = new BaseGupiaoKline();
                    kzz1.setSymbol(symbol);
                    kzz1.setPeriod(period);

                    kzz1.setTimestamp(DateTimeUtil.parseToDate(jsonArray1[0]));
                    kzz1.setBizDate(jsonArray1[0]);

                    kzz1.setOpen(Double.parseDouble(jsonArray1[1]));
                    kzz1.setClose(Double.parseDouble(jsonArray1[2]));
                    kzz1.setHigh(Double.parseDouble(jsonArray1[3]));
                    kzz1.setLow(Double.parseDouble(jsonArray1[4]));

                    kzz1.setVolume(Double.parseDouble(jsonArray1[5]));
                    kzz1.setAmount(jsonArray1[6]);

                    kzz1.setPs(jsonArray1[7]);//振幅(百分比)
                    kzz1.setPercent(Double.parseDouble(jsonArray1[8])); //涨幅(百分比)
                    kzz1.setChg(Double.parseDouble(jsonArray1[9]));//涨跌
                    kzz1.setTurnoverrate(Double.parseDouble(jsonArray1[10])); //换手
                    list.add(kzz1);
                } catch (Exception e) {
                    log.debug(e.toString());
                }
            }
            gupiaoManager.saveKlineAll(list);
            gupiaoManager.updateTime(symbol);
        } catch (Exception e) {
            log.debug(e.toString());
        }

    }
}
