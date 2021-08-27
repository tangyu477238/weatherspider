package cn.zifangsky.spider;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.UserAgentUtils;
import cn.zifangsky.model.ProxyIp;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

public class ProxyIPSpider3 implements PageProcessor {

	@Override
	public Site getSite() {
		Site site = Site.me().setTimeOut(4000).setRetryTimes(3)
				.setSleepTime(1000).setCharset("UTF-8").addHeader("Accept-Encoding", "/")
				.setUserAgent(UserAgentUtils.radomUserAgent());
		
		return site;
	}

	@Override
	public void process(Page page) {
		try {
			List<String> ipList = page.getHtml().xpath("//table[@class='bg']/tbody/tr[@class='cells']").all();
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
					proxyIp.setAddr(data[3]);
					proxyIp.setType(data[2]);
					proxyIp.setOther("list.proxylistplus.com");
					result.add(proxyIp);
				} catch (Exception e) {

				}
			}
			if (ComUtil.isEmpty(result)){
				return;
			}
			page.putField("result", result);
		} catch (Exception e){

		}
	}

}
