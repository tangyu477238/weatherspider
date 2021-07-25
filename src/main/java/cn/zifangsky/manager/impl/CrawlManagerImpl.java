package cn.zifangsky.manager.impl;

import cn.zifangsky.manager.CrawlManager;
import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.spider.*;
import cn.zifangsky.spider.gp.JslPipeline;
import cn.zifangsky.spider.gp.JslSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;
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






	@Override
	public void proxyIPCrawl() {
		try {
			OOSpider.create(new ProxyIPSpider9())
					.addUrl("https://www.kuaidaili.com/free/inha/1/").addPipeline(proxyIPPipeline)
					.thread(1)
					.run();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			OOSpider.create(new ProxyIPSpider()).setDownloader(httpClientManager.getHttpClientDownloader())
					.addUrl("https://ip.jiangxianli.com/?page=1").addPipeline(proxyIPPipeline)
					.thread(2)
					.run();
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			OOSpider.create(new ProxyIPSpider3()).setDownloader(httpClientManager.getHttpClientDownloader())
					.addUrl("https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-1").addPipeline(proxyIPPipeline)
					.thread(2)
					.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			OOSpider.create(new ProxyIPSpider5()).setDownloader(httpClientManager.getHttpClientDownloader())
					.addUrl("http://www.xiladaili.com/gaoni/").addPipeline(proxyIPPipeline)
					.thread(2)
					.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			OOSpider.create(new ProxyIPSpider5()).setDownloader(httpClientManager.getHttpClientDownloader())
					.addUrl("http://www.xiladaili.com/http/").addPipeline(proxyIPPipeline)
					.thread(2)
					.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			OOSpider.create(new ProxyIPSpider5()).setDownloader(httpClientManager.getHttpClientDownloader())
					.addUrl("http://www.xiladaili.com/https/").addPipeline(proxyIPPipeline)
					.thread(2)
					.run();
		} catch (Exception e) {
			e.printStackTrace();
		}




	}

	@Override
	public void proxyIPCrawl2() {
		try {
			OOSpider.create(new ProxyIPSpider2()).setDownloader(httpClientManager.getHttpClientDownloader())
					.addUrl("http://www.89ip.cn/index_1.html").addPipeline(proxyIPPipeline)
					.thread(2)
					.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			OOSpider.create(new ProxyIPSpider4()).setDownloader(httpClientManager.getHttpClientDownloader())
					.addUrl("http://www.66ip.cn/1.html").addPipeline(proxyIPPipeline)
					.thread(2)
					.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			OOSpider.create(new ProxyIPSpider6()).setDownloader(httpClientManager.getHttpClientDownloader())
					.addUrl("http://www.ip3366.net/?stype=1&page=1").addPipeline(proxyIPPipeline)
					.thread(2)
					.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public void getDataJsl() {
		OOSpider.create(new JslSpider()).addPipeline(jslPipeline)
//				.setDownloader(getHttpClientDownloader())
				.addUrl("https://www.jisilu.cn/data/cbnew/cb_list/?___jsl=LST___t=1600677498531")
				.thread(2)
				.run();
	}

	@Override
	public void getDataJslDetail(String bondId) {
		OOSpider.create(new JslSpider()).addPipeline(jslPipeline)
//				.setDownloader(getHttpClientDownloader())
				.addUrl("https://www.jisilu.cn/data/cbnew/detail_hist/"+bondId)
				.thread(1)
				.run();
	}









}
