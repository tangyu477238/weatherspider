package cn.zifangsky.login;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

/**
 * 利用HttpClient进行post请求的工具类
 * @ClassName: HttpClientUtil
 * @Description: TODO
 * @author Devin <xxx>
 * @date 2017年2月7日 下午1:43:38
 *
 */
public class HttpClientUtil {



    @SuppressWarnings("resource")
    public static String doPostJson(String url,String jsonstr){
        return doPost(url, jsonstr, "application/json");
    }


    @SuppressWarnings("resource")
    public static String doPostForm(String url,String jsonstr){
        return doPost(url, jsonstr, "application/x-www-form-urlencoded");
    }


    public static String doPost(String url,String jsonstr,String ContentType){
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", ContentType);
            httpPost.setEntity(new StringEntity(jsonstr));
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,"utf-8");
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }


    public static String get(String url) {

         String result = "";
         try {
             HttpClient httpClient = new SSLClient();
             HttpGet httpGet = new HttpGet(url);
             httpGet.addHeader("Content-type", "application/json; charset=utf-8");
             httpGet.setHeader("Accept", "application/json");
             HttpResponse response = httpClient.execute(httpGet);
             if(response != null){
                 HttpEntity resEntity = response.getEntity();
                 if(resEntity != null){
                     result = EntityUtils.toString(resEntity,"utf-8");
                 }
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         return result;
     }

}