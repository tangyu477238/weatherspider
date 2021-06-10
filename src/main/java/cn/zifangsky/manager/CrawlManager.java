package cn.zifangsky.manager;

public interface CrawlManager {
	
	/**
	 * 天气爬虫
	 * @param stationCode 县城（区）的CODE
	 */
	 void weatherCrawl(String stationCode);
	
	/**
	 * 代理IP爬虫，地址：http://www.xicidaili.com
	 */
	 void proxyIPCrawl();
	
	/**
	 * 代理IP爬虫，地址：http://www.kuaidaili.com
	 */
	 void proxyIPCrawl2();

	/**
	 * trainCrawl，
	 */
	 void trainCrawl(String date,String fromName,String toName);

	/**
	 * trainPriceCrawl，
	 */
//	 void trainPriceCrawl(String trainNo,String fromCode,String toCode,String bizDate,String seatTypes);

	/**
	 * trainInfoCrawl，
	 */
	 void trainInfoCrawl(String trainNo,String fromCode,String toCode,String bizDate);

	 void getDataJsl();
	 void getDataJslDetail(String bondId);


}
