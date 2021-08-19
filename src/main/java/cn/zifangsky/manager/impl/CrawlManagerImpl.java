package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.emuns.ProxyUrlEnum;
import cn.zifangsky.manager.CrawlManager;
import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.spider.*;
import cn.zifangsky.spider.gp.JslPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service("crawlManager")
public class CrawlManagerImpl implements CrawlManager {

	
	@Resource(name="proxyIPPipeline")
	private ProxyIPPipeline proxyIPPipeline;


	@Resource(name="jslPipeline")
	private JslPipeline jslPipeline;



	@Resource(name="httpClientManager")
	private HttpClientManager httpClientManager;


	/****** 完整的流程是这样的

	Spider.create(new GithubRepoPageProcessor())
		//从https://github.com/code4craft开始抓
		.addUrl("https://github.com/code4craft")
		//设置Scheduler，使用Redis来管理URL队列
		.setScheduler(new RedisScheduler("localhost"))
		//设置Pipeline，将结果以json方式保存到文件
		.addPipeline(new JsonFilePipeline("D:\\data\\webmagic"))
		//开启5个线程同时执行
		.thread(5)
		//启动爬虫
		.run();
	*****/


	/**
	 * 启动抓取数据
	 * @param proxyFlag
	 */
	@Override
	public void proxyIPCrawl(boolean proxyFlag) {
		try {
			List<String> list = new ArrayList<>();
			for (ProxyUrlEnum proxyUrlEnum : ProxyUrlEnum.values()) {
				list.add(proxyUrlEnum.getCode());
			}
			Collections.shuffle(list);
			String url = list.get(0);
			PageProcessor  pageProcessor = (PageProcessor) Class.forName(ProxyUrlEnum.getProxyUrl(url)).newInstance();
			Spider spider = OOSpider.create(pageProcessor)
					.addUrl(url).addPipeline(proxyIPPipeline);
			if (proxyFlag){
				HttpClientDownloader httpClientDownloader = httpClientManager.getCheckHttpClientDownloader();
				if (ComUtil.isEmpty(httpClientManager)){
					return;
				}
				spider.setDownloader(httpClientDownloader);
			}
			spider.thread(1).run();
		} catch (Exception e) {
			log.debug(e.toString());
		}
	}

	@Override
	public void proxyIPCrawl() {
		proxyIPCrawl(true);
	}

	@Override
	public void getIPCrawl() {
		proxyIPCrawl(false);
	}


//	public void getDataJsl() {
//		OOSpider.create(new JslSpider()).addPipeline(jslPipeline)
//				.addUrl("https://www.jisilu.cn/data/cbnew/cb_list/?___jsl=LST___t=1600677498531")
//				.thread(2)
//				.run();
//	}
//
//
//	public void getDataJslDetail(String bondId) {
//		OOSpider.create(new JslSpider()).addPipeline(jslPipeline)
//				.addUrl("https://www.jisilu.cn/data/cbnew/detail_hist/"+bondId)
//				.thread(1)
//				.run();
//	}









}
