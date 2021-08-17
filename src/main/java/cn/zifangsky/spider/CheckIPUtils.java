package cn.zifangsky.spider;

import cn.zifangsky.model.ProxyUrl;
import cn.zifangsky.repository.ProxyUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.*;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CheckIPUtils {

	@Resource
	private ProxyUrlRepository proxyUrlRepository;

    private  boolean checkValidIP(String ip,Integer port, URL url){

		HttpURLConnection connection = null;
		try {
			//代理服务器
			InetSocketAddress proxyAddr = new InetSocketAddress(ip, port);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
			connection = (HttpURLConnection) url.openConnection(proxy);
			connection.setReadTimeout(4000);
			connection.setConnectTimeout(4000);
			connection.setRequestMethod("GET");
			if(connection.getResponseCode() == 200){
				log.info(MessageFormat.format("============代理IP[{0} {1}]可用=====================", ip,port));
				return true;
			}

		} catch (java.net.NoRouteToHostException e) {
			return true;
		} catch (ConnectException e) {
			return false;
		}  catch (java.net.SocketException e) {
			return false;
		} catch (javax.net.ssl.SSLHandshakeException e) {
			return false;
		} catch (java.lang.RuntimeException e) {
			return false;
		} catch (SocketTimeoutException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(MessageFormat.format("代理IP[{0} {1}]不可用", ip,port));
		} finally {
			if(connection != null){
				connection.disconnect();
			}
		}
		return false;
	}


	/**
	 * 校验代理IP的有效性，测试地址为：http://www.ip138.com
	 * @param ip 代理IP地址
	 * @param port  代理IP端口
	 * @return  此代理IP是否有效
	 */
	public  boolean checkValidIP(String ip, Integer port){
		try{
			List<ProxyUrl> list = proxyUrlRepository.findAll();
			Collections.shuffle(list);
			for (ProxyUrl proxyUrl : list){
				URL url = new URL(proxyUrl.getUrl());
				if (checkValidIP(ip, port, url)){
					return  true;
				}
			}
		} catch (Exception e) {
			log.debug(e.toString());
		}
		return false;
	}


}
