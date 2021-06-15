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
				.addHeader("Cookie","PHPSESSID=4bhg3nijgadtihe80odeld81m1; safedog-flow-item=62079EBB764DF5FCCFA2E59587774A8E")
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
			Html html = Html.create(tmp);
			BizOrder bizOrder = new BizOrder();
			String str = html.xpath("//body/text()").toString().trim();
			String[] data = str.split("\\s+");
			bizOrder.setOrderNo(data[0]);
			bizOrder.setCreateTime(data[1]+" "+data[2]);
			bizOrder.setBizDate(data[3]+" "+data[4]);
			bizOrder.setRouteName(data[5]);
			bizOrder.setInfo(data[6]);
			bizOrder.setNum(new BigDecimal(data[7]));
			bizOrder.setAmount(new BigDecimal(data[8]));
			bizOrder.setName(data[9]);
			bizOrder.setMobile(data[10]);
			bizOrder.setState(data[11]);
			bizOrder.setPlatForm(data[13]);
			result.add(bizOrder);
		}
		page.putField("result", result);
	}


}
