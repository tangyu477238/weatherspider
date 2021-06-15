package cn.zifangsky.manager;

public interface CrawlManager {
	

	/**
	 * 代理IP爬虫，地址：http://www.xicidaili.com
	 */
	 void proxyIPCrawl();
	
	/**
	 * 代理IP爬虫，地址：http://www.kuaidaili.com
	 */
	 void proxyIPCrawl2();



	 void getDataJsl();
	 void getDataJslDetail(String bondId);


}
