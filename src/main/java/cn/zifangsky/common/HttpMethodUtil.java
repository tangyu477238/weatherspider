package cn.zifangsky.common;



import cn.zifangsky.enums.DongfangEnum;
import cn.zifangsky.enums.KlineEnum;
import cn.zifangsky.login.StockUtil;
import cn.zifangsky.model.ProxyIp;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.*;
import java.util.Date;
@Slf4j
public class HttpMethodUtil {
    public static String doPost(String url,String params){
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        String content = null;
        StringBuffer sbf = new StringBuffer();
        try{
            URL u = new URL(url);
            conn = (HttpURLConnection)u.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setReadTimeout(2000);
            conn.setConnectTimeout(2000);
            conn.setRequestProperty("accept","*/*");
            conn.setRequestProperty("connection","Keep-Alive");
            conn.setRequestProperty("content-Type","application/json");

            writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
            writer.print(params);
            writer.flush();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while((content = reader.readLine())!=null){
                sbf.append(content);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(writer!=null){
                writer.close();
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            conn.disconnect();
        }
        return sbf.toString();

    }

    public static String doGet(String url, ProxyIp proxyIp){
        StringBuffer sbf = new StringBuffer();
        HttpURLConnection conn = null;
        BufferedReader br = null;
        String content = null;
        try{
            URL u = new URL(url);
            if (ComUtil.isEmpty(proxyIp)) {
                conn = (HttpURLConnection) u.openConnection();
            } else {
                conn = (HttpURLConnection)u.openConnection(getProxy(proxyIp));
            }
            conn.setReadTimeout(2000);
            conn.setConnectTimeout(2000);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if(conn.getResponseCode()==200){
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
                while((content=br.readLine())!=null){
                    sbf.append(content);
                }
            }
        } catch (SocketTimeoutException e) {
            doGet(url);
        } catch(Exception e) {
//            e.printStackTrace();
            log.info(e.toString());
        } finally{
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            conn.disconnect();
        }
        return sbf.toString();
    }


    public static String doGet(String url){
        return doGet(url,null);
    }


    public static Proxy getProxy(ProxyIp proxyIp){
        InetSocketAddress proxyAddr = new InetSocketAddress(proxyIp.getIp(), proxyIp.getPort());
        Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
        return proxy;
    }



    /***
     * 获取 url
     * @param bondId
     * @param period
     * @param beg
     * @return
     */
    public static String getUrl(String bondId, Integer period, String beg){
        int exchange_type =  StockUtil.isShenshi(bondId)  ? 0 : 1; //深/沪
        StringBuffer url = new StringBuffer("http://");
        url.append(System.currentTimeMillis())
                .append(DongfangEnum.KLINE.getUrl())
                .append("&klt=").append(period)
                .append("&fqt=1&secid=").append(exchange_type).append(".").append(bondId)
                .append("&beg=").append(beg)
                .append("&end=20500000&_=1618930055730");
//        log.info(url.toString());
        return url.toString();
    }


    public static Integer getBizDate(Integer period){
        int num = 500;
        if (period== KlineEnum.K_5M.getId()){
            num = Double.valueOf(Math.ceil(500/48)).intValue();
        } else if (period== KlineEnum.K_30M.getId()){
            num = Double.valueOf(Math.ceil(500/8)).intValue();
        } else if (period== KlineEnum.K_1D.getId()){
            num = 500;
        } else if (period== KlineEnum.K_1W.getId()){
            num = 500*5;
        }
        return num;
    }
}
