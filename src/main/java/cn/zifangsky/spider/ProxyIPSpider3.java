package cn.zifangsky.spider;

import cn.zifangsky.model.ProxyIp;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyIPSpider3 implements PageProcessor {

	@Override
	public Site getSite() {
		Site site = Site.me().setTimeOut(6000).setRetryTimes(3)
				.setSleepTime(1000).setCharset("UTF-8").addHeader("Accept-Encoding", "/")
				.setUserAgent(UserAgentUtils.radomUserAgent());
		
		return site;
	}

	@Override
	public void process(Page page) {
		try {
		List<String> ipList = page.getHtml().xpath("//table[@class='bg']/tbody/tr[@class='cells']").all();
		List<ProxyIp> result = new ArrayList<>();
	
		if(ipList != null && ipList.size() > 0){
			for(String tmp : ipList){

					Html html = Html.create(tmp);
					ProxyIp proxyIp = new ProxyIp();
					String[] data = html.xpath("//body/text()").toString().trim().split("\\s+");
					proxyIp.setIp(data[0]);
					proxyIp.setPort(Integer.valueOf(data[1]));
					proxyIp.setAddr(data[3]);
					proxyIp.setType(data[2]);
					proxyIp.setOther("list.proxylistplus.com");
					result.add(proxyIp);

			} 
		}

		} catch (Exception e){

		}
	}

}
