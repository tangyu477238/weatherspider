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

public class ProxyIPSpider4 implements PageProcessor {

	@Override
	public Site getSite() {
		Site site = Site.me().setTimeOut(6000).setRetryTimes(3)
				.setSleepTime(1000).setCharset("gb2312").addHeader("Accept-Encoding", "/")
				.setUserAgent(UserAgentUtils.radomUserAgent());
		
		return site;
	}

	@Override
	public void process(Page page) {
		try {
			List<String> ipList = page.getHtml().xpath("//div[@class='container']/div/div/table/tbody/tr").all();
			List<ProxyIp> result = new ArrayList<>();
			if (ComUtil.isEmpty(ipList)){
				return;
			}
			ipList.remove(0); //移除表头
			for (String tmp : ipList) {
				try {
					Html html = Html.create(tmp);
					ProxyIp proxyIp = new ProxyIp();
					String[] data = html.xpath("//body/text()").toString().trim().split("\\s+");
					proxyIp.setIp(data[0]);
					proxyIp.setPort(Integer.valueOf(data[1]));
					proxyIp.setAddr(data[2]);
					proxyIp.setType(data[3]);
					proxyIp.setOther("www.66ip.cn");
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
