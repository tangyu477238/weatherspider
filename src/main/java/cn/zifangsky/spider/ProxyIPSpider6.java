package cn.zifangsky.spider;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.model.ProxyIp;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyIPSpider6 implements PageProcessor {

	@Override
	public Site getSite() {
		Site site = Site.me().setTimeOut(6000).setRetryTimes(3)
				.setSleepTime(1000).setCharset("gb2312").addHeader("Accept-Encoding", "gzip, deflate").addHeader("Host","www.ip3366.net")
				.addHeader("Cookie","Hm_lvt_c4dd741ab3585e047d56cf99ebbbe102=1606984745; Hm_lpvt_c4dd741ab3585e047d56cf99ebbbe102=1607677129")
				.setUserAgent(UserAgentUtils.radomUserAgent());
		return site;
	}

	@Override
	public void process(Page page) {
		try {
			List<String> ipList = page.getHtml().xpath("//table[@class='table table-bordered table-striped']/tbody/tr").all();
			List<ProxyIp> result = new ArrayList<>();
			if (ComUtil.isEmpty(ipList)){
				return;
			}

			for(String tmp : ipList){
				try {
					Html html = Html.create(tmp);
					ProxyIp proxyIp = new ProxyIp();
					String[] data = html.xpath("//body/text()").toString().trim().split("\\s+");
					proxyIp.setIp(data[0]);
					proxyIp.setPort(Integer.valueOf(data[1]));
					proxyIp.setAddr(data[6]);
					proxyIp.setType(data[3]);
					proxyIp.setOther("www.ip3366.net");
					result.add(proxyIp);
				} catch (Exception e) {

				}
			}

			if (ComUtil.isEmpty(result)){
				return;
			}
			page.putField("result", result);
		}catch (Exception e){

		}
	}

}
