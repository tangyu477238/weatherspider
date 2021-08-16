package cn.zifangsky.spider.gp;

import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.manager.XueqiuManager;
import cn.zifangsky.model.GupiaoKline;
import cn.zifangsky.mq.producer.GupiaoKlineSender;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义Pipeline处理抓取的数据
 * @author zifangsky
 *
 */
@Component("xueqiuGupiaoKlinePipeline")
public class XueqiuGupiaoKlinePipeline implements Pipeline {

	@Resource(name="gupiaoKlineSender")
	private GupiaoKlineSender gupiaoKlineSender;

	@Resource
	private XueqiuManager xueqiuManager;
	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {

		long timestamp = System.currentTimeMillis();
		String result = resultItems.get("result");
		String url = resultItems.getRequest().getUrl();
		Map<String, String> map = urlSplit(url);
		String symbol = map.get("symbol");
		String period = map.get("period");
		JSONArray jsonArray = JSONObject.parseObject(result).getJSONObject("data").getJSONArray("item");
		for (int i = 0; jsonArray!=null && i < jsonArray.size(); i++) {
			JSONArray jsonArray1 = jsonArray.getJSONArray(i);
			GupiaoKline kzz1 = new GupiaoKline();
			kzz1.setSymbol(symbol);
//			kzz1.setPeriod(period);
			Long time =jsonArray1.getLong(0);
			if (i==0){
				timestamp = time;
			}
			kzz1.setTimestamp(new Date(time));
//			kzz1.setBizDate(getBizDate(kzz1.getPeriod(),kzz1.getTimestamp()));

			kzz1.setVolume(jsonArray1.getDouble(1));
			kzz1.setOpen(jsonArray1.getDouble(2));
			kzz1.setHigh(jsonArray1.getDouble(3));
			kzz1.setLow(jsonArray1.getDouble(4));
			kzz1.setClose(jsonArray1.getDouble(5));
			kzz1.setChg(jsonArray1.getDouble(6));
			kzz1.setPercent(jsonArray1.getDouble(7));
			kzz1.setTurnoverrate(jsonArray1.getDouble(8));
			kzz1.setAmount(jsonArray1.getString(9));

			//检测任务添加到队列中
			gupiaoKlineSender.send(kzz1);

		}

		//sendNewPost(symbol, period, timestamp, jsonArray);
	}

	private void sendNewPost(String symbol,String period, long timestamp, JSONArray jsonArray){
		if (period.equals("day") && jsonArray.size()==800){
			try { Thread.sleep(3000);} catch (InterruptedException ie){}//n为毫秒数
			xueqiuManager.getDataXueqiuDetailKline(symbol.toUpperCase(),period,timestamp);
			return ;
		}
		return ;
	}

	private String getBizDate(String period, Date Timestamp){
		if (period.equals("day")){
			return DateTimeUtil.formatDateTimetoString(Timestamp, DateTimeUtil.FMT_yyyyMMdd);
		}
		if (period.indexOf("m")>-1){
			return DateTimeUtil.formatDateTimetoString(Timestamp, DateTimeUtil.FMT_yyyyMMddHHmm);
		}
		return "";
	}



	/**
	 * 去掉url中的路径，留下请求参数部分
	 * @param strURL url地址
	 * @return url请求参数部分
	 * @author lzf
	 */
	private static String TruncateUrlPage(String strURL){
		String strAllParam=null;
		String[] arrSplit=null;
		strURL=strURL.trim().toLowerCase();
		arrSplit=strURL.split("[?]");
		if(strURL.length()>1){
			if(arrSplit.length>1){
				for (int i=1;i<arrSplit.length;i++){
					strAllParam = arrSplit[i];
				}
			}
		}
		return strAllParam;
	}

	/**
	 * 解析出url参数中的键值对
	 * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
	 * @param URL  url地址
	 * @return  url请求参数部分
	 * @author lzf
	 */
	public static Map<String, String> urlSplit(String URL){
		Map<String, String> mapRequest = new HashMap<String, String>();
		String[] arrSplit=null;
		String strUrlParam=TruncateUrlPage(URL);
		if(strUrlParam==null){
			return mapRequest;
		}
		arrSplit=strUrlParam.split("[&]");
		for(String strSplit:arrSplit){
			String[] arrSplitEqual=null;
			arrSplitEqual= strSplit.split("[=]");
			//解析出键值
			if(arrSplitEqual.length>1){
				//正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
			}else{
				if(arrSplitEqual[0]!=""){
					//只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}
}
