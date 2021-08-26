package cn.zifangsky.manager;

import cn.zifangsky.model.ProxyIp;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

public interface HttpClientManager {

    HttpClientDownloader getHttpClientDownloader();

    HttpClientDownloader getCheckHttpClientDownloader();

    HttpClientDownloader getHttpClientDownloader(ProxyIp proxyIp);
}
