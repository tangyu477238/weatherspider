package cn.zifangsky.manager;

import us.codecraft.webmagic.downloader.HttpClientDownloader;

public interface HttpClientManager {

    HttpClientDownloader getHttpClientDownloader();
}
