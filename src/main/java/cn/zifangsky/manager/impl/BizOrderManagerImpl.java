package cn.zifangsky.manager.impl;

import cn.zifangsky.manager.BizOrderManager;
import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.spider.pipeline.BizOrderPipeline;
import cn.zifangsky.spider.gp.BizOrderSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;

@Slf4j
@Service("bizOrderManager")
public class BizOrderManagerImpl implements BizOrderManager {


	@Resource
	private BizOrderPipeline bizOrderPipeline;



	@Resource(name="httpClientManager")
	private HttpClientManager httpClientManager;


	@Override
	public void getOrder(String bizDate) {
		OOSpider.create(new BizOrderSpider()).addPipeline(bizOrderPipeline)
//				.setDownloader(getHttpClientDownloader())
				.addUrl("http://beiwan.gzwkh.com/vanke/admin/index.php/tp/index1?tck_no=&date="+bizDate+"&_name=&_cardID=&_phone=&act=search")
				.thread(2)
				.run();
	}









}
