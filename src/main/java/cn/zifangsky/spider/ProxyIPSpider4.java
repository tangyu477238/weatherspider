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
		Site site = Site.me().setTimeOut(4000).setRetryTimes(3)
				.setSleepTime(1000).setCharset("UTF-8").addHeader("Accept-Encoding", "/")
				.setUserAgent(UserAgentUtils.radomUserAgent());
		
		return site;
	}

	@Override
	public void process(Page page) {
		try {
			List<String> ipList = page.getHtml().xpath("//table[@class='table table-hover table-bordered']/tbody/tr").all();
			List<ProxyIp> result = new ArrayList<>();
			if (ComUtil.isEmpty(ipList)){
				return;
			}
//			ipList.remove(0); //移除表头
			for (String tmp : ipList) {
				try {
					String strs [] = tmp.split("\n");
					String strs0 [] = strs[1].split(">");
					String strs03 = strs0[3].replace("</a","");
					String strs1 = strs[2].replace("<td>","").replace("</td>","");
					ProxyIp proxyIp = new ProxyIp();
					proxyIp.setIp(strs03.trim());
					proxyIp.setPort(Integer.valueOf(strs1.trim()));
					proxyIp.setAddr("");
					proxyIp.setType("");
					proxyIp.setOther("ip.ihuan.me");
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
