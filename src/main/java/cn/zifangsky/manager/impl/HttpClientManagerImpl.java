package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.ProxyIp;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import javax.annotation.Resource;

@Service("httpClientManager")
public class HttpClientManagerImpl implements HttpClientManager {

	@Resource(name="proxyIpManager")
	private ProxyIpManager proxyIpManager;

	@Override
	public  HttpClientDownloader getHttpClientDownloader(){
		return getHttpClientDownloader(proxyIpManager.selectRandomIP());
	}

	@Override
	public HttpClientDownloader getCheckHttpClientDownloader() {
		return getHttpClientDownloader(proxyIpManager.selectCheckRandomIP());
	}


	@Override
	public HttpClientDownloader getHttpClientDownloader(ProxyIp proxyIp) {
		if (ComUtil.isEmpty(proxyIp)){
			return null;
		}
		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
		httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(proxyIp.getIp(), proxyIp.getPort())));
		return httpClientDownloader;
	}
}
