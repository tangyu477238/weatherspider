package cn.zifangsky.spider.gp;

import cn.zifangsky.common.HttpClientUtil;
import cn.zifangsky.common.UserAgentUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.HashSet;
import java.util.Set;


public class XueqiuSpider implements PageProcessor{
	
	private Site site = Site.me().setTimeOut(2000).setRetryTimes(3)
			.setSleepTime(2000).setCharset("UTF-8");
	
	@Override
	public Site getSite() {
		String cookie = HttpClientUtil.getCookie("https://xueqiu.com/");
		Set<Integer> acceptStatCode = new HashSet<>();
		acceptStatCode.add(200);
		site = site.setAcceptStatCode(acceptStatCode)
				.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
				.addHeader("Accept-Encoding", "gzip, deflate, br")
				.addHeader("Accept-Language", "zh-CN,zh;q=0.9")
				.addHeader("Cache-Control", "max-age=0")
				.addHeader("Connection", "keep-alive")
				//.addHeader("Host","xxx.com")
				.addHeader("Cookie","device_id=7747c2bd6caa3341d305a592a0a33d5c; s=e811w1awef; Hm_lvt_1db88642e346389874251b5a1eded6e3=1612363542;"+cookie+" is_overseas=0; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1613287306")
				.setUserAgent(UserAgentUtils.radomUserAgent());
		return site;
	}

	@Override
	public void process(Page page) {
		Json json= page.getJson();

		String result = json.toString();
		JSONArray jsonArray = JSONObject.parseObject(result).getJSONObject("data").getJSONArray("list");
		page.putField("result",jsonArray.toString());
	}


}
