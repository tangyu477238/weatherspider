package cn.zifangsky.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.MessageFormat;

@Slf4j
@Service
public class CheckIPUtils {
    private  boolean checkValidIP(String ip,Integer port, URL url){

		HttpURLConnection connection = null;
		try {
			//代理服务器
			InetSocketAddress proxyAddr = new InetSocketAddress(ip, port);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
			connection = (HttpURLConnection) url.openConnection(proxy);
			connection.setReadTimeout(2000);
			connection.setConnectTimeout(2000);
			connection.setRequestMethod("GET");
			StringBuffer msg = new StringBuffer();
			if(connection.getResponseCode() == 200){
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) { // 循环从流中读取
					msg.append(line).append("\n");
				}
				reader.close(); // 关闭流
			}

			if (msg.toString().startsWith("jQuery")){
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
//			List<ProxyUrl> list = proxyUrlRepository.findAll();
//			Collections.shuffle(list);
//			for (int i = 0 ; i<list.size() ; i++){
//				URL url = new URL(list.get(i).getUrl());
//				if (i==5){
//					return false;
//				}
//				if (checkValidIP(ip, port, url)){
//					return  true;
//				}
//			}
			StringBuffer url = new StringBuffer("http://");
					url.append(System.currentTimeMillis());
					url.append(".push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery112403780605306048155_1618930055627");
					url.append("&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&ut=7eea3edcaed734bea9cbfc24409ed989&klt=30&fqt=1&secid=0.000001&beg=20210827&end=20210827&_=1618930055730");
			return checkValidIP(ip, port, new URL(url.toString()));
		} catch (Exception e) {
			log.debug(e.toString());
		}
		return false;
	}




}
