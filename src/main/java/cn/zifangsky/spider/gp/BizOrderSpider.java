package cn.zifangsky.spider.gp;

import cn.zifangsky.model.BizOrder;
import cn.zifangsky.spider.UserAgentUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BizOrderSpider implements PageProcessor{
	
	private Site site = Site.me().setTimeOut(20000).setRetryTimes(3)
			.setSleepTime(2000).setCharset("UTF-8");
	
	@Override
	public Site getSite() {
		Set<Integer> acceptStatCode = new HashSet<>();
		acceptStatCode.add(200);
		site = site.setAcceptStatCode(acceptStatCode)
				.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
				.addHeader("Accept-Encoding", "gzip, deflate, br")
				.addHeader("Accept-Language", "zh-CN,zh;q=0.9")
				.addHeader("Cache-Control", "max-age=0")
				.addHeader("Connection", "keep-alive")
//				.addHeader("Host","www.jisilu.cn")
				.addHeader("Cookie","PHPSESSID=2m1pt4v4d2cm2k04bt3s6vqpu2; safedog-flow-item=7ED64C710BB5401FB78C4CE372EC5D6D")
				.setUserAgent(UserAgentUtils.radomUserAgent());
		return site;
	}

	@Override
	public void process(Page page) {
		List<String> ipList = page.getHtml().xpath("//table[@border='0']/tbody/tr").all();
		List<BizOrder> result = new ArrayList<>();
		if(ipList.isEmpty()){
			return;
		}
		ipList.remove(0);
		for(String tmp : ipList){
			String[] data = tmp.replace("</td","").replace("&gt;","").split(">");


//			Html html = Html.create(tmp);
			BizOrder bizOrder = new BizOrder();
//			String str = html.xpath("//body/text()").toString().trim();
//			String[] data = str.split("\\s+");
			if (data.length<25){
				continue;
			}
			bizOrder.setOrderNo(data[2]);
			bizOrder.setCreateTime(data[4]);
			bizOrder.setBizDate(data[6]);
			bizOrder.setRouteName(data[8]);
			bizOrder.setInfo(data[10]);
			bizOrder.setNum(new BigDecimal(data[12]));
			bizOrder.setAmount(new BigDecimal(data[14]));
			bizOrder.setName(data[16]);
			bizOrder.setMobile(data[18]);
			bizOrder.setState(data[20]);
			bizOrder.setPlatForm(data[24]);
			result.add(bizOrder);
		}
		page.putField("result", result);
	}


}
