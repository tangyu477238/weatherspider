package cn.zifangsky.spider.gp;

import cn.zifangsky.common.UserAgentUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class XinlangSpider implements PageProcessor{
	
	private Site site = Site.me().setTimeOut(20000).setRetryTimes(3)
			.setSleepTime(2000).setCharset("gb2312");
	
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
				.addHeader("Cookie","SINAGLOBAL=111.196.247.105_1593935414.669550; UOR=www.baidu.com,k.sina.com.cn,; UM_distinctid=173402a1427413-05770c59ea663a-4353760-1fa400-173402a142842a; U_TRS1=00000016.8ddf5a6f.5f0c7041.f254b845; SCF=Ak2js2yfEnRuHNV5FOzZcKZGBBsJgFlu0Kc_-2cCMgXf0o7fwTtAmi142K7-8DYgbPF8yCqePl88M5iwguWIQa8.; SR_SEL=1_511; lxlrttp=1578733570; __gads=ID=bd9273abaabe70fe-22a4e92af5c40073:T=1606611983:RT=1606611983:R:S=ALNI_MZj-D7ehm_P0HgIt5GfFD64Obb-4A; Apache=111.196.246.81_1607732564.124959; _ga=GA1.3.822690120.1607732565; ULV=1607732628535:8:2:2:111.196.246.81_1607732564.124959:1607732565019; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFAbOLhPfLI.VKBf.0_bJqa5NHD95QNehefSK.ceh-4Ws4DqcjiUs9DMcnEeh24; U_TRS2=00000051.1d2cfd11.5fd40d94.8f56f1f9; SUB=_2A25y9FUEDeRhGeFO6FcU-SjMzjWIHXVRgMHMrDV_PUNbm9AKLVXnkW9NQWbxO0jQWQwEkCBcieLmPz2RK4Fa5xTZ; ALF=1641109716; hqEtagMode=1; MONEY-FINANCE-SINA-COM-CN-WEB5=; FIN_ALL_VISITED=sh600268; rotatecount=8; FINA_V_S_2=sh600268,sh600519,sh000001")
				.setUserAgent(UserAgentUtils.radomUserAgent());
		return site;
	}

	@Override
	public void process(Page page) {
		String str = page.getHtml().xpath("//h1[@id='stockName']/span").get();
		Html html1 = Html.create(str);
		String data1 = html1.xpath("//span/text()").toString();
		data1 = data1.substring(1,7);
		List<String> ipList = page.getHtml().xpath("//table[@class='comInfo1'][2]/tbody/tr").all();
		List<String> result = new ArrayList<>();

		if(ipList != null && ipList.size() > 0){
			for(String tmp : ipList){
				Html html = Html.create(tmp);
				String[] data = html.xpath("//body/text()").toString().trim().split("\\s+");
				result.add(data[0]);
			}
		}
		result.remove(1);
		result.remove(0);
		page.putField("result", result);
		page.putField("symbol", data1);
	}


}
