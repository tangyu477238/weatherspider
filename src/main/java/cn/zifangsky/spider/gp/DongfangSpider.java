package cn.zifangsky.spider.gp;

import cn.zifangsky.login.StockUtil;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.spider.UserAgentUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class DongfangSpider implements PageProcessor{

	private Site site = Site.me().setTimeOut(20000).setRetryTimes(3)
			.setCharset("UTF-8")
			.setSleepTime(2000);
	
	@Override
	public Site getSite() {
		Set<Integer> acceptStatCode = new HashSet<>();
		acceptStatCode.add(200);
		site = site.setAcceptStatCode(acceptStatCode)
				.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
				.addHeader("Accept-Encoding", "gzip, deflate")
				.addHeader("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6")
				.addHeader("Cache-Control", "max-age=0")
				.addHeader("Connection", "keep-alive")
				.addHeader("Host","push2his.eastmoney.com")
				.addHeader("Upgrade-Insecure-Requests", "1")
//				.addHeader("Cookie","em_hq_fls=js; intellpositionL=1522.39px; intellpositionT=755px; st_si=84906325244496; emshistory=%5B%22%E5%B9%B3%E5%AE%89%E9%93%B6%E8%A1%8C%22%5D; HAList=a-sz-000001-%u5E73%u5B89%u94F6%u884C%2Ca-sz-300059-%u4E1C%u65B9%u8D22%u5BCC%2Ca-sz-300579-%u6570%u5B57%u8BA4%u8BC1; em-quote-version=topspeed; st_asi=delete; qgqp_b_id=1c4495e1baa8916811221b3358b11543; st_pvi=86076381304229; st_sp=2020-07-06%2022%3A20%3A22; st_inirUrl=https%3A%2F%2Fwww.baidu.com%2Flink; st_sn=212; st_psi=20210421204718713-113200301201-8481318564")
				.setUserAgent(UserAgentUtils.radomUserAgent());
		return site;
	}

	@Override
	public void process(Page page) {
		Json json = page.getJson();

		String result = json.toString();
		result = result.split("\\(")[1].split("\\)")[0];
		JSONObject object = JSONObject.parseObject(result).getJSONObject("data");
		List<Gupiao> list = new ArrayList();
		JSONArray jsonArray = object.getJSONArray("diff");
		log.debug(jsonArray.toJSONString());
		for (int i = 0; jsonArray!=null && i < jsonArray.size(); i++) {
			JSONObject object1 =  JSONObject.parseObject(jsonArray.get(i).toString());
			Gupiao gupiao = new Gupiao();

//			kzz1.setOpen(Double.parseDouble(jsonArray1[1]));
//			kzz1.setClose(Double.parseDouble(jsonArray1[2]));
//			kzz1.setHigh(Double.parseDouble(jsonArray1[3]));
//			kzz1.setLow(Double.parseDouble(jsonArray1[4]));
//			kzz1.setVolume(Double.parseDouble(jsonArray1[5]));
//			kzz1.setAmount(jsonArray1[6]);

//			kzz1.setPs(jsonArray1[7]);//振幅(百分比)
//			kzz1.setPercent(Double.parseDouble(jsonArray1[8])); //涨幅(百分比)
//			kzz1.setChg(Double.parseDouble(jsonArray1[9]));//涨跌
//			kzz1.setTurnoverrate(Double.parseDouble(jsonArray1[10])); //换手

			gupiao.setPercent(object1.getString("f3").equals("-")?0:object1.getDouble("f3"));//涨幅(百分比)
			gupiao.setVolume(object1.getString("f5").equals("-")?0:object1.getLong("f5"));
			gupiao.setAmount(object1.getString("f6").equals("-")?0:object1.getLong("f6"));
			gupiao.setPs(object1.getString("f7").equals("-")?0:object1.getDouble("f7"));//振幅(百分比)
			gupiao.setSymbol(object1.getString("f12"));
			gupiao.setName(object1.getString("f14"));
			gupiao.setType(StockUtil.isShenshi(gupiao.getSymbol())  ? 0 : 1); //深/沪
			list.add(gupiao);
		}
		page.putField("result", list);
	}


}
