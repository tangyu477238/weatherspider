package cn.zifangsky.spider.gp;

import cn.zifangsky.common.UserAgentUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.HashSet;
import java.util.Set;


public class DongfangKlineSpider implements PageProcessor{
	
	private Site site = Site.me().setTimeOut(2000).setRetryTimes(3)
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
		page.putField("result", json.toString());
	}


}
