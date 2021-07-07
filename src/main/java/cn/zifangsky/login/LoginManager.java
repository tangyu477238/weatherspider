package cn.zifangsky.login;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

@Slf4j
@Service
public class LoginManager {

    String comp_id = "1091";

    @Value("${cczq.hs_openid}")
    private String hs_openid ;
    @Value("${cczq.fund_account}")
    private String fund_account ;
    @Value("${cczq.h_stock_account}")
    private String h_stock_account ;
    @Value("${cczq.s_stock_account}")
    private String s_stock_account ;

    @Value("${cczq.access_token}")
    private String access_token;


    /****
     * 可用仓位
     * @return
     * @throws Exception
     */
    public int getEnableAmount(String stock_code) throws Exception{
        return JSONUtil.parseObj(queryStockEnablelNum(stock_code)).getJSONObject("data").getInt("enable_amount");
    }

    /**
     * 当前仓位
     * @param stock_code
     * @return
     * @throws Exception
     */
    public int getCurrentAmount(String stock_code) throws Exception{
        return  JSONUtil.parseObj(queryStockEnablelNum ( stock_code)).getJSONObject("data").getInt("current_amount");
    }


    /****
     * 补仓
     * @return
     * @throws Exception
     */
    public int buchongStockNum () throws Exception{
        String stock_code = "159949";
        Integer enableAmount = getEnableAmount( stock_code);
        Integer currentAmount = getCurrentAmount( stock_code);
        Integer initNum = 4000;
        if (currentAmount<initNum) { //当前<4000 需要补充仓位
            return currentAmount-initNum;
        }
        //当前(6000)>4000 ,可用仓位(2000) > (6000-4000)
        if (currentAmount>initNum && enableAmount>0){
            return enableAmount < (currentAmount - initNum) ? enableAmount:(currentAmount - initNum);
        }
        return 0;
    }

    /****
     * getNewPriceCyb
     * 最新价格
     */
    public Double getNewPriceCyb() throws Exception{
        String stock_code = "159949";
        String stock_name = "创业板50";
        Double newPrice = Double.parseDouble(getNewPrice(stock_code));
        return newPrice;
    }




    /****
     * 定价卖出

     */
    public void hungSellCyb ( String original_price, String current_price, int entrust_amount) throws Exception{

        String stock_code = "159949";
        String stock_name = "创业板50";
//        String original_price = ""; //触发价格
//        String current_price = original_price; //当前价格(无效)
//        int entrust_amount = 2000; //委托数

        hungSell(stock_code, stock_name, original_price, current_price, entrust_amount );
    }

    /****
     * 挂单买入

     */
    public void hungBuyCyb ( String original_price, String current_price,int entrust_amount) throws Exception{

        String stock_code = "159949";
        String stock_name = "创业板50";
//        String original_price = original_price; //触发价格
//        String current_price = current_price; //当前价格
//        int entrust_amount = 2000; //委托数

        hungBuy(stock_code, stock_name, original_price, current_price, entrust_amount );
    }

    /***
     * 创建创业板表格
     * @param base_price
     * @return
     * @throws Exception
     */
    public String gridYmdCyb (String base_price) throws Exception{
        String stock_code = "159949";
        String stock_name = "创业板50";
//        String base_price  = "1.500"; //基准价格
        String lower_limit = "1.000";
        String upper_limit = "2.000";
        String increase = "1.00";
        String decrease = "1.00";
        String close_after_entrust_failure= "false";

        String current_price= base_price;
        int position_upper_limit=10000;
        int position_lower_limit=1000;
        int entrust_amount = 1000; //委托数
        String json = gridYmd( stock_code,  stock_name,  base_price, lower_limit, upper_limit,increase, decrease, close_after_entrust_failure,
                current_price,  position_upper_limit,  position_lower_limit,  entrust_amount );
        return JSONUtil.parseObj(json).getStr("data");
    }


    /****
     * getNewPrice

     */
    public String getNewPrice(String stock_code) throws Exception{
        int exchange_type =  isShenshi(stock_code)  ? 2 : 1; //深/沪
        String url = "https://tjd.cczq.com:5000/cczq/biz/v/getCodeInfosByExchangeType?codes="+stock_code+"."+exchange_type;
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.info(httpOrgCreateTestRtn);
        String str = StringEscapeUtils.unescapeJava(httpOrgCreateTestRtn);
        log.info(str);
        //只去两端的如下两行
        str = str.substring(13,str.length()-2);
        log.info(str);
        JSONObject jsonObject = JSONUtil.parseObj(str);
        return jsonObject.getStr("newPrice");
    }

    /***
     *  仓位情况
     * @return
     * @throws Exception
     */
    public String queryMyStockAmount () throws Exception{

        String url = "https://tjd.cczq.com:5000/cczq/biz/v/queryMyStockAmount?"+getAccountInfo()+getImeiInfo();
        log.info("");
        log.info(url);
        log.info("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.info(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }

    /***
     * 清除任务
     * @throws Exception
     */
    public void deleteAllMyYmd () throws Exception{
        String json = queryMyYmdForPage();
        JSONArray jsonArray = JSONUtil.parseObj(json).getJSONArray("data");
        for (Object object : jsonArray){
            JSONObject jsonObject = (JSONObject)object;
            String ymdId = jsonObject.getJSONObject("ymd_base").getStr("ymd_id");
            deleteYmd(ymdId);
        }
//
    }

    /***
     *  监控的计划
     * @return
     * @throws Exception
     */
    public String queryMyYmdForPage () throws Exception{


        String page_start_p1="";
        String page_start_p2="";
        String page_size="10";
        String search_status="1";
        String stock_code="";
        String cep_type="1";

        String date_type="5";
        String order_column="update_datetime";
        String order_direction="desc";


        String url = "https://tjd.cczq.com:5000/cczq/biz/v/queryMyYmdForPage?page_start_p1="+page_start_p1
                +"&page_start_p2="+page_start_p2+"&page_size="+page_size+"&search_status="+search_status
                +"&stock_code="+stock_code+"&cep_type="+cep_type
                +"&strategy_ids=8%2C34%2C35%2C16%2C9%2C7%2C15%2C22%2C12&date_type="+date_type
                +"&order_column="+order_column+"&order_direction="+order_direction+"&"+getAccountInfo() +getImeiInfo();
        log.info("");
        log.info(url);
        log.info("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.info(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }


    /***
     * 获取当前仓位和可用仓位
     * @return
     * @throws Exception
     */
    public String queryStockEnablelNum (String stock_code) throws Exception{
        
        String url = "https://tjd.cczq.com:5000/cczq/biz/v/queryStockEnablelNum?stock_code="+stock_code
                +"&"+getAccountInfo() +getImeiInfo()+stockAccount(stock_code);

        log.info("");
        log.info(url);
        log.info("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.info(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }







    /***
     *  网格交易
     * @param stock_code
     * @param stock_name
     * @param base_price
     * @param lower_limit
     * @param upper_limit
     * @param increase
     * @param decrease
     * @param close_after_entrust_failure
     * @param current_price
     * @param position_upper_limit
     * @param position_lower_limit
     * @param entrust_amount
     *  
     * @return
     * @throws Exception
     */
    public String gridYmd(String stock_code, String stock_name, String base_price,String lower_limit,String upper_limit,
                                  String increase,String decrease,String close_after_entrust_failure,
                                  String current_price, int position_upper_limit, int position_lower_limit, int entrust_amount ) throws Exception{

        String entrust_price_mode = "NewPrice,NewPrice"; //即时价格

        String url = "https://tjd.cczq.com:5000/cczq/biz/v/gridYmd?stock_code="+stock_code
                +"&base_price="+base_price+"&lower_limit="+lower_limit +"&upper_limit="+upper_limit+"&increase="+increase
                +"&decrease="+decrease+"&close_after_entrust_failure="+close_after_entrust_failure
                +"&current_price="+current_price
                +"&position_upper_limit="+position_upper_limit+"&position_lower_limit="+position_lower_limit
                +"&entrust_amount="+entrust_amount
                +"&entrust_price_mode="+entrust_price_mode+"&"+getAccountInfo() +getImeiInfo()+ getStockAccount(stock_code)+ getPriceInfo(stock_name);
        log.info("");
        log.info(url);
        log.info("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.info(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }

    /***
     * 均价突破
     * @param stock_code
     * @param stock_name
     * @param trigger_mode
     * @param avg_line
     * @param duration
     * @param entrust_bs
     * @param current_price
     * @param entrust_amount
     *  
     * @return
     * @throws Exception
     */
    public String avgLineYmd(String stock_code, String stock_name, int trigger_mode,int avg_line,int duration,int entrust_bs,
                                     String current_price, int entrust_amount ) throws Exception{


        String entrust_price_mode = "NewPrice"; //即时价格

        String url = "https://tjd.cczq.com:5000/cczq/biz/v/avgLineYmd?stock_code="+stock_code
                +"&trigger_mode="+trigger_mode+"&avg_line="+avg_line
                +"&duration="+duration+"&entrust_bs="+entrust_bs
                +"&current_price="+current_price
                +"&entrust_amount="+entrust_amount
                +"&entrust_price_mode="+entrust_price_mode+"&"+getAccountInfo() +getImeiInfo()+ getStockAccount(stock_code)+ getPriceInfo(stock_name);
        log.info("");
        log.info(url);
        log.info("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.info(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }

    /**
     *  删除 任务
     * @param ymd_id
     *  
     * @return
     * @throws Exception
     */
    public String deleteYmd(String ymd_id ) throws Exception{

        String url = "https://tjd.cczq.com:5000/cczq/biz/v/deleteYmd?ymd_id="+ymd_id+"&is_delete=0"
                +"&"+getAccountInfo() +getImeiInfo();
        log.info(url);
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);

        return httpOrgCreateTestRtn;
    }


    /****
     * 挂单买入
     * @param stock_code
     * @param stock_name
     * @param original_price
     * @param current_price
     * @param entrust_amount
     *  
     * @return
     * @throws Exception
     */
    public String hungBuy(String stock_code, String stock_name, String original_price, String current_price,
                                  int entrust_amount ) throws Exception{


        String entrust_price_mode = "NewPrice"; //即时价格


        String url = "https://tjd.cczq.com:5000/cczq/biz/v/hungBuy?stock_code="+stock_code
                +"&original_price="+original_price
                +"&current_price="+current_price
                +"&entrust_amount="+entrust_amount
                +"&entrust_price_mode="+entrust_price_mode+"&"+getAccountInfo() +getImeiInfo()+ getStockAccount(stock_code)+ getPriceInfo(stock_name);
        log.info("");
        log.info(url);
        log.info("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.info(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }



    /****
     * 定价卖出
     * @param stock_code
     * @param stock_name
     * @param original_price
     * @param current_price
     * @param entrust_amount
     *  
     * @return
     * @throws Exception
     */
    public String hungSell(String stock_code, String stock_name, String original_price, String current_price,
                                 int entrust_amount ) throws Exception{





        String entrust_price_mode = "NewPrice"; //即时价格



        String url = "https://tjd.cczq.com:5000/cczq/biz/v/hungSell?stock_code="+stock_code
                +"&original_price="+original_price
                +"&current_price="+current_price
                +"&entrust_amount="+entrust_amount
                +"&entrust_price_mode="+entrust_price_mode + "&"+getAccountInfo() +getImeiInfo() + getStockAccount(stock_code)+ getPriceInfo(stock_name);
        log.info("");
        log.info(url);
        log.info("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.info(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }


    /****
     * 止盈止损
     * @param stock_code
     * @param stock_name
     * @param base_price
     * @param stop_profit_rate
     * @param stop_loss_rate
     * @param stop_loss_price
     * @param stop_profit_price
     * @param current_price
     * @param entrust_amount
     *  
     * @return
     * @throws Exception
     */
    public String stopProfitAndLoss(String stock_code, String stock_name,
                                           String base_price, String stop_profit_rate,String stop_loss_rate,String stop_loss_price,String stop_profit_price,
                                           String current_price, int entrust_amount ) throws Exception{




        String entrust_price_mode = "NewPrice"; //即时价格


        String url = "https://tjd.cczq.com:5000/cczq/biz/v/stopProfitAndLoss?stock_code="+stock_code
                +"&base_price="+base_price
                +"&stop_profit_rate="+stop_profit_rate+"&stop_loss_rate="+stop_loss_rate
                +"&stop_loss_price="+stop_loss_price+"&stop_profit_price="+stop_profit_price
                +"&current_price="+current_price+"&entrust_amount="+entrust_amount
                +"&entrust_price_mode="+entrust_price_mode+ "&"+getAccountInfo() + getImeiInfo() + getStockAccount(stock_code) + getPriceInfo(stock_name);
        log.info("");
        log.info(url);
        log.info("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.info(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }

    private String getPriceInfo(String stock_name)throws Exception{
        int entrust_price_type = 1; //限价委托
        String expiry_days = "2021-07-26 15:00:00"; //失效日期+时间
        expiry_days = URLEncoder.encode(expiry_days, "UTF-8");
        expiry_days = expiry_days.replaceAll("\\+", "%20");
        String expiry_days_text = "";
        stock_name = URLEncoder.encode(stock_name, "UTF-8");

        return "&expiry_days="+expiry_days+"&expiry_days_text="+expiry_days_text+"&entrust_price_type="+entrust_price_type+"&stock_name="+stock_name;

    }


    private String getImeiInfo() throws Exception{
        String op_station = "MA;IIP:111.196.241.22;IPORT:NA;LIP:192.168.16.10;MAC:5CC307738AE8;IMEI:NA;RMPN:13552379492;UMPN:+8613552379492;ICCID:NA;OSV:ANDROID10;IMSI:NA@TDXADR;V5.00;HSTJD";
        op_station = URLEncoder.encode(op_station, "UTF-8");
        String ext = "{\"channel\":null}";
        ext = URLEncoder.encode(ext, "UTF-8");
        return "&op_station="+op_station+"&ext="+ext;
    }

    private String getAccountInfo(){
        return "comp_id="+comp_id+"&hs_openid="+hs_openid
                +"&access_token="+access_token+"&fund_account="+fund_account;
    }

    private String getStockAccount(String stock_code){
        int cep_type = 1;
        return "&cep_type="+cep_type+stockAccount(stock_code);
    }

    private String stockAccount(String stock_code){
        int exchange_type = isShenshi(stock_code)  ? 2 : 1; //深/沪
        String stock_account =  isShenshi(stock_code) ? s_stock_account : h_stock_account; //沪市或深市
        return "&exchange_type="+exchange_type+"&stock_account="+stock_account;
    }
    private boolean isShenshi(String stock_code){
        return !stock_code.startsWith("60") && !stock_code.startsWith("11") && !stock_code.startsWith("51");
    }
}
