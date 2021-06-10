package cn.zifangsky.spider.train;

import cn.zifangsky.spider.UserAgentUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.HashSet;
import java.util.Set;


public class TrainSpider implements PageProcessor{
	
	private Site site = Site.me().setTimeOut(20000).setRetryTimes(3)
			.setSleepTime(2000).setCharset("UTF-8");
	
	@Override
	public Site getSite() {
		Set<Integer> acceptStatCode = new HashSet<>();
		acceptStatCode.add(200);
		site = site.setAcceptStatCode(acceptStatCode).addHeader("Accept-Encoding", "/")
				.addHeader("Cookie","JSESSIONID=6452A136651D7CEEF03F850EE95E7D39; tk=VGGzfClATXitH93k1yS73VwIjJVkUjwdfRpT8MHrCtMaft1t0; BIGipServerotn=1072693770.64545.0000; RAIL_EXPIRATION=1599869157065; RAIL_DEVICEID=azK1lBp8c0Kcds6JlWyX7VBYbm2AYuzn-Qxk_ZtgSRUe5_fSFLQZ1dgZziENL1rHhvhCKJsF4oVrwpIb8lO44glU6GkT9eNUdwrlOm2XnwKZwvMFJzb0sMhhw0pNFKr1w_TNki7vgmK_3RXlmZA5Evo4PfjVyMtf; BIGipServerpool_passport=367854090.50215.0000; route=9036359bb8a8a461c164a04f8f50b252; uKey=0dedcbe37ee406bee6bcb3a3b829744f0649a290472d835923b6efd51a87cb92\n")
				.setUserAgent(UserAgentUtils.radomUserAgent());

		return site;
	}

	@Override
	public void process(Page page) {
		Json json= page.getJson();
		String str = json.toString();
		page.putField("trainInfo", str);
	}


}
