package cn.zifangsky.spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.text.MessageFormat;

public class CheckIPUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckIPUtils.class);
	
	/**
	 * 校验代理IP的有效性，测试地址为：http://www.ip138.com
	 * @param ip 代理IP地址
	 * @param port  代理IP端口
	 * @return  此代理IP是否有效
	 */
	public static boolean checkValidIP(String ip,Integer port){
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL("http://quote.eastmoney.com");
			//代理服务器
			InetSocketAddress proxyAddr = new InetSocketAddress(ip, port);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
			connection = (HttpURLConnection) url.openConnection(proxy);
			connection.setReadTimeout(4000);
			connection.setConnectTimeout(4000);
			connection.setRequestMethod("GET");

			if(connection.getResponseCode() == 200){
				LOGGER.info(MessageFormat.format("============代理IP[{0} {1}]可用=====================", ip,port));
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
            LOGGER.error(MessageFormat.format("代理IP[{0} {1}]不可用", ip,port));
		} finally {
            if(connection != null){
                connection.disconnect();
            }
        }
		return false;
	}

	public static void main(String[] args) {
		CheckIPUtils.checkValidIP("114.249.115.239",9000);
	}
}
