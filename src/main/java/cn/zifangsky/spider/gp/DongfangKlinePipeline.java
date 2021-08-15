package cn.zifangsky.spider.gp;

import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.common.StringUtil;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.model.GupiaoKline;
import cn.zifangsky.model.GupiaoKline5m;
import cn.zifangsky.mq.producer.GupiaoKlineSender;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 自定义Pipeline处理抓取的数据
 * @author zifangsky
 *
 */
@Component("dongfangKlinePipeline")
@Slf4j
public class DongfangKlinePipeline implements Pipeline {

	@Resource(name="gupiaoManager")
	private GupiaoManager gupiaoManager;


	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {

		try {
//		long timestamp = System.currentTimeMillis();
			String result = resultItems.get("result");
			result = result.split("\\(")[1].split("\\)")[0];
			JSONObject object = JSONObject.parseObject(result).getJSONObject("data");

			String url = resultItems.getRequest().getUrl();
			Map<String, String> map = StringUtil.urlSplit(url);
			String symbol = object.getString("code");
			String period = getPeriod(map.get("klt"));
			JSONArray jsonArray = object.getJSONArray("klines");
			log.debug(jsonArray.toJSONString());
			List<BaseGupiaoKline> list = new ArrayList<>();
			for (int i = 0; jsonArray != null && i < jsonArray.size(); i++) {
				String jsonArray1[] = jsonArray.get(i).toString().split(",");
				BaseGupiaoKline kzz1;
				if (period.equals("5m")) {
					kzz1 = new GupiaoKline5m();
				} else {
					kzz1 = new GupiaoKline();
				}

				kzz1.setSymbol(symbol);
				kzz1.setPeriod(period);
				try {
					kzz1.setTimestamp(DateTimeUtil.parseToDate(jsonArray1[0]));
				} catch (Exception e) {
					e.printStackTrace();
				}
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
			}
			gupiaoManager.saveKlineAll(list);
		} catch (Exception e){
			log.info(e.toString());
		}
	}

	private String getPeriod(String period){
		int periodNum = Integer.parseInt(period);
		if (periodNum<100){
			return period+"m";
		}
		if (periodNum==101){
			return "day";
		}
		return null;
	}
//	private void sendNewPost(String symbol,String period, long timestamp, JSONArray jsonArray){
//		if (period.equals("day") && jsonArray.size()==800){
//			try { Thread.sleep(3000);} catch (InterruptedException ie){}//n为毫秒数
//			xueqiuManager.getDataXueqiuDetailKline(symbol.toUpperCase(),period,timestamp);
//			return ;
//		}
//		return ;
//	}

	private String getBizDate(String period, Date Timestamp){
		if (period.equals("day")){
			return DateTimeUtil.formatDateTimetoString(Timestamp, DateTimeUtil.FMT_yyyyMMdd);
		}
		if (period.indexOf("m")>-1){
			return DateTimeUtil.formatDateTimetoString(Timestamp, DateTimeUtil.FMT_yyyyMMddHHmm);
		}
		return "";
	}


}
