package cn.zifangsky.spider.gp;

import cn.zifangsky.common.UserAgentUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.HashSet;
import java.util.Set;


public class JslSpider implements PageProcessor{
	
	private Site site = Site.me().setTimeOut(2000).setRetryTimes(3)
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
				.addHeader("Cookie","device_id=7747c2bd6caa3341d305a592a0a33d5c; xq_a_token=1132205e8c57eb587b26526804cff9f3b6bf6799; xqat=1132205e8c57eb587b26526804cff9f3b6bf6799; xq_r_token=81b9c911ea3907729d8f8e9f60d9f5251227c551; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOi0xLCJpc3MiOiJ1YyIsImV4cCI6MTYwOTEyMzA1NywiY3RtIjoxNjA2NjEyNTM0MjY3LCJjaWQiOiJkOWQwbjRBWnVwIn0.hSaMI18uQij40u8Y11fxTqXhUhdO4tk2boQ9xeZk1pl6cHII5rrSn43yO5zZXKSIgSB-Pyj3JFvnwEQGN6oUxU-0bxQCS6IKLu9yNiIL7Z6sGZrNoOoo-zj-wCZAdtuXjiQDITswe681bgqFU6kNrtLxdmFt-b0kW7HSdw3_p0zMrvWFsn0piG-F6YBZ58uyasuCzNhbCRFf5m8ydBnxvcLxWuio-XZ3D5-cYtpAABuogNJnSjg_FeWkxpKUoGwQX-Ld76TN59WU8huQGvt-jgtt_hJfvRQsBMLszgjGMhYNEVUW06Jj4h6v1Smhc7XN0vVHaXG2P61aZcmI4mlzRA; u=331606612561772; Hm_lvt_1db88642e346389874251b5a1eded6e3=1604826668,1606612572; s=e811w1awef; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1606637255")
				.setUserAgent(UserAgentUtils.radomUserAgent());
		return site;
	}

	@Override
	public void process(Page page) {
		Json json= page.getJson();
		page.putField("result", json.toString());
	}


}
