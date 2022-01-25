package cn.zifangsky.login;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LoginManager implements ILogin{

    @Resource
    private CczqConfig cczqConfig;

    @Resource
    private GupiaoCanUseRepository gupiaoCanUseRepository;

    public String getStartTime(){
        return gupiaoCanUseRepository.getStartTime(cczqConfig.getHs_openid());
    }
    public String getEndTime(){
        return gupiaoCanUseRepository.getEndTime(cczqConfig.getHs_openid());
    }
    public String getAccessToken(){
        return gupiaoCanUseRepository.getToken(cczqConfig.getHs_openid());
    }

    /****
     * 可用仓位
     * @return
     * @throws Exception
     */
    public int getEnableAmount(String stock_code) throws Exception{
        JSONObject info = queryStockEnablelNum(stock_code);
        if (ComUtil.isEmpty(info)){
            return -1;
        }
        return info.getJSONObject("data").getInt("enable_amount");
    }

    /**
     * 当前仓位
     * @param stock_code
     * @return
     * @throws Exception
     */
    public int getCurrentAmount(String stock_code) throws Exception{
        JSONObject info = queryStockEnablelNum(stock_code);
        if (ComUtil.isEmpty(info)){
            return -1;
        }
        return  info.getJSONObject("data").getInt("current_amount");
    }




    /****
     * getNewPrice
     * 获取最新价格
     */
    public String getNewPrice(String stock_code){
        int exchange_type =  StockUtil.isShenshi(stock_code)  ? 2 : 1; //深/沪
        String url = "https://tjd.cczq.com:5000/cczq/biz/v/getCodeInfosByExchangeType?codes="+stock_code+"."+exchange_type;
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.debug(httpOrgCreateTestRtn);
        String str = StringEscapeUtils.unescapeJava(httpOrgCreateTestRtn);
        log.debug(str);
        //只去两端的如下两行
        str = str.substring(13,str.length()-2);
        log.debug(str);
        JSONObject jsonObject = JSONUtil.parseObj(str);
        return jsonObject.getStr("newPrice");
    }

    /***
     * 指定产品
     * 获取当前仓位和可用仓位
     * @return
     * @throws Exception
     */
    public JSONObject queryStockEnablelNum(String stock_code) throws Exception{

        String url = "https://tjd.cczq.com:5000/cczq/biz/v/queryStockEnablelNum?stock_code="+stock_code
                +"&"+getAccountInfo() +getImeiInfo()+stockAccount(stock_code);
        log.debug("");
        log.debug(url);
        log.debug("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.debug(httpOrgCreateTestRtn);
        JSONObject jsonObject = JSONUtil.parseObj(httpOrgCreateTestRtn);
        if ("100006".equals(jsonObject.getStr("error_no"))){
            return null;
        }
        return jsonObject;
    }

    /***
     *  所有产品仓位情况
     * @return
     * @throws Exception
     */
    public List<JSONObject> queryMyStockAmount() throws Exception{
        String url = "https://tjd.cczq.com:5000/cczq/biz/v/queryMyStockAmount?"+getAccountInfo()+getImeiInfo();
        log.info(cczqConfig.getHs_openid());
        log.debug(url);
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        JSONObject jsonObj = JSONUtil.parseObj(httpOrgCreateTestRtn);
        List<JSONObject> list = new ArrayList<>();
        if (jsonObj.getInt("total") == 0){
            return list;
        }
        JSONArray jsonArray = jsonObj.getJSONArray("data");
        for (Object object : jsonArray){
            JSONObject jsonObject = (JSONObject)object;
            Integer enable_amount = jsonObject.getInt("enable_amount");
            if(enable_amount == 0){
                continue;
            }
            list.add(jsonObject);
        }
        return list;
    }

    /***
     * 清除所有或指定任务
     * @throws Exception
     */
    public void deleteAllMyYmd(String strategyId) throws Exception{
        String json = queryMyYmdForPage();
        JSONArray jsonArray = JSONUtil.parseObj(json).getJSONArray("data");
        for (Object object : jsonArray){
            JSONObject jsonObject = (JSONObject)object;
            String ymdId = jsonObject.getJSONObject("ymd_base").getStr("ymd_id");
            String strategy_id = jsonObject.getJSONObject("ymd_base").getStr("strategy_id");
            if (ComUtil.isEmpty(strategyId)){
                deleteYmd(ymdId);
                continue;
            }
            if (strategyId.equals(strategy_id)){
                deleteYmd(ymdId);
            }
        }
//
    }

    /**
     * 清除已没有数量的
     * 条件单
     * @throws Exception
     */
    public void clearStockYmd() throws Exception{
        //获取条件列表
        Map<String, String> ymdMap = listMyYmd();
        ymdMap.forEach((k, v) -> {
            try {
                String stock_code = k.split("_")[0];
                int currentNum = getCurrentAmount(stock_code); //当前数量
                if (currentNum==0){
                    deleteYmd(v.split("_")[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /***
     *  监控的计划
     * @return
     * @throws Exception
     */
    public String queryMyYmdForPage() throws Exception{


        String page_start_p1="";
        String page_start_p2="";
        String page_size="30";
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
        log.debug("");
        log.debug(url);
        log.debug("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.debug(httpOrgCreateTestRtn);
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
        log.debug("");
        log.debug(url);
        log.debug("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.debug(httpOrgCreateTestRtn);
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
        log.debug("");
        log.debug(url);
        log.debug("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.debug(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }

    /**
     *  删除 任务
     * @param ymd_id
     *  
     * @return
     * @throws Exception
     */
    public String deleteYmd(String ymd_id) throws Exception{

        String url = "https://tjd.cczq.com:5000/cczq/biz/v/deleteYmd?ymd_id="+ymd_id+"&is_delete=0"
                +"&"+getAccountInfo() +getImeiInfo();
        log.debug(url);
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);

        return httpOrgCreateTestRtn;
    }

    /***
     * 按单个产品买入
     * @param stock_code
     * @param stock_name
     * @param entrust_amount
     * @throws Exception
     */
    public void hungBuyByStoreCode(String stock_code, String stock_name, int entrust_amount) throws Exception{
        //获取最新价格
        BigDecimal newPrice  = new BigDecimal(getNewPrice(stock_code));
        BigDecimal val = oddOrEven() ? new BigDecimal(0.001) : new BigDecimal(-0.001);
        //获取触发价格
        String original_price = String.valueOf(newPrice.add(val)
                .setScale(3, BigDecimal.ROUND_HALF_UP));
        hungBuy(stock_code, stock_name, original_price, newPrice.toString(), entrust_amount);
    }


    public static boolean oddOrEven() {
        long a = System.currentTimeMillis();
        if ((a & 1) == 1) {
            return true;
        } else {
            return false;
        }
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


        String entrust_price_mode = "SellPrice5"; //即时价格


        String url = "https://tjd.cczq.com:5000/cczq/biz/v/hungBuy?stock_code="+stock_code
                +"&original_price="+original_price
                +"&current_price="+current_price
                +"&entrust_amount="+entrust_amount
                +"&entrust_price_mode="+entrust_price_mode+"&"+getAccountInfo() +getImeiInfo()+ getStockAccount(stock_code)+ getPriceInfo(stock_name);
        log.debug("");
        log.debug(url);
        log.debug("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.debug(httpOrgCreateTestRtn);
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
                                 int entrust_amount) throws Exception{

        String entrust_price_mode = "BuyPrice5"; //即时价格 NewPrice

        String url = "https://tjd.cczq.com:5000/cczq/biz/v/hungSell?stock_code="+stock_code
                +"&original_price="+original_price +"&current_price="+current_price +"&entrust_amount="+entrust_amount
                +"&entrust_price_mode="+entrust_price_mode + "&"+getAccountInfo() +getImeiInfo() + getStockAccount(stock_code)+ getPriceInfo(stock_name);
        log.debug("");
        log.debug(url);
        log.debug("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.debug(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }

    /***
     * 根据产品编码，卖出指定数量
     * @param stock_code
     * @param stock_name
     * @param enable_amount
     * @throws Exception
     */
    public void hungSellByStoreCode(String stock_code, String stock_name, Integer enable_amount) throws Exception{
        BigDecimal newPrice  = new BigDecimal(getNewPrice(stock_code)); //获取最新价格
        String original_price = String.valueOf(newPrice.subtract(new BigDecimal(-0.001))
                .setScale(3, BigDecimal.ROUND_HALF_UP)); //获取触发价格
        String original_price2 = String.valueOf(newPrice.subtract(new BigDecimal(0.001))
                .setScale(3, BigDecimal.ROUND_HALF_UP)); //获取触发价格
        hungSell(stock_code, stock_name, original_price, newPrice.toString(), enable_amount);
        hungSell(stock_code, stock_name, original_price2, newPrice.toString(), enable_amount);
    }



    /**
     *  get
     * @return
     * @throws Exception
     */
    public Map listMyYmd() throws Exception{
        Map map = new HashMap();
        String json = queryMyYmdForPage();
        JSONArray jsonArray = JSONUtil.parseObj(json).getJSONArray("data");
        for (Object object : jsonArray){
            JSONObject jsonObject = (JSONObject)object;
            JSONObject stock = jsonObject.getJSONObject("ymd_trade");
            JSONObject strategy = jsonObject.getJSONObject("ymd_base");
            JSONObject condition = jsonObject.getJSONObject("ymd_condition");
            String key = appendStr(stock.getStr("stock_code"), strategy.getStr("strategy_id"));
            String value = appendStr(stock.getStr("entrust_amount"), strategy.getStr("ymd_id"));
            value = appendStr(value, condition.getStr("original_price"));
            map.put(key, value);
        }
        return map;
    }

    /****
     * 回落
     * @param stock_code
     * @param stock_name
     * @param original_price 触发
     * @param decline_rate 回落
     * @param current_price
     * @param entrust_amount
     *
     * @return
     * @throws Exception
     */
    public String risedownSell(String stock_code, String stock_name,
                                    String original_price, String decline_rate,
                                    String current_price, int entrust_amount) throws Exception{
        String entrust_price_mode = "BuyPrice5"; //即时价格 NewPrice
        String url = "https://tjd.cczq.com:5000/cczq/biz/v/risedownSell?stock_code="+stock_code
                +"&original_price="+original_price+"&decline_rate="+decline_rate
                +"&current_price="+current_price+"&entrust_amount="+entrust_amount
                +"&entrust_price_mode="+entrust_price_mode+ "&"+getAccountInfo() + getImeiInfo() + getStockAccount(stock_code) + getPriceInfo(stock_name);
        log.debug("");
        log.debug(url);
        log.debug("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.debug(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }

    /***
     * 止盈止损
     * @param stock_code
     * @param stock_name
     * @param base_price
     * @param stop_profit_rate
     * @param stop_profit_price
     * @param stop_loss_rate
     * @param stop_loss_price
     * @param current_price
     * @param entrust_amount
     * @return
     * @throws Exception
     */
    public String stopProfitAndLoss(String stock_code, String stock_name, String base_price,
                                    String stop_profit_rate, String stop_profit_price, String stop_loss_rate, String stop_loss_price,
                                    String current_price, int entrust_amount ) throws Exception{

        String entrust_price_mode = "BuyPrice5"; //即时价格 BuyPrice
        String url = "https://tjd.cczq.com:5000/cczq/biz/v/stopProfitAndLoss?stock_code="+stock_code
                +"&base_price="+base_price
                +"&stop_profit_rate="+stop_profit_rate+"&stop_loss_rate="+stop_loss_rate
                +"&stop_loss_price="+stop_loss_price+"&stop_profit_price="+stop_profit_price
                +"&current_price="+current_price+"&entrust_amount="+entrust_amount
                +"&entrust_price_mode="+entrust_price_mode+ "&"+getAccountInfo() + getImeiInfo() + getStockAccount(stock_code) + getPriceInfo(stock_name);
        log.debug("");
        log.debug(url);
        log.debug("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.debug(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }
    //&expiry_days=xx&expiry_days_text=xx&entrust_price_type=xx&stock_name=xx
    private String getPriceInfo(String stock_name)throws Exception{
        int entrust_price_type = 1; //限价委托
        String expiry_days = DateTimeUtil.getBeforeDay(30) + " 15:00:00"; //失效日期+时间
        expiry_days = URLEncoder.encode(expiry_days, "UTF-8");
        expiry_days = expiry_days.replaceAll("\\+", "%20");
        String expiry_days_text = "";
        stock_name = URLEncoder.encode(stock_name, "UTF-8");

        return "&expiry_days="+expiry_days+"&expiry_days_text="+expiry_days_text+"&entrust_price_type="+entrust_price_type+"&stock_name="+stock_name;

    }

    //&op_station=1&ext=xx
    private String getImeiInfo() throws Exception{
        String op_station = "MA;IIP:111.196.241.22;IPORT:NA;LIP:192.168.16.10;MAC:5CC307738AE8;IMEI:NA;RMPN:13552379492;UMPN:+8613552379492;ICCID:NA;OSV:ANDROID10;IMSI:NA@TDXADR;V5.00;HSTJD";
        op_station = URLEncoder.encode(op_station, "UTF-8");
        String ext = "{\"channel\":null}";
        ext = URLEncoder.encode(ext, "UTF-8");
        return "&op_station="+op_station+"&ext="+ext;
    }
    //&comp_id=1&hs_openid=xx&access_token=xx&fund_account=xx
    private String getAccountInfo(){
        return "comp_id="+cczqConfig.getCompId()+"&hs_openid="+cczqConfig.getHs_openid()
                +"&access_token="+getAccessToken()+"&fund_account="+cczqConfig.getFund_account();
    }
    //&cep_type=1&exchange_type=xx&stock_account=xx
    private String getStockAccount(String stock_code){
        return "&cep_type=1"+stockAccount(stock_code);
    }
    //&exchange_type=xx&stock_account=xx
    private String stockAccount(String stock_code){
        int exchange_type = StockUtil.isShenshi(stock_code)  ? 2 : 1; //深/沪
        String stock_account =  StockUtil.isShenshi(stock_code) ? cczqConfig.getS_stock_account() : cczqConfig.getH_stock_account(); //沪市或深市
        return "&exchange_type="+exchange_type+"&stock_account="+stock_account;
    }






//    public void addRisedownSell(Map map,String stock_code, String stock_name, Integer enable_amount, String newPrice) throws Exception{
//        if (checkAddYmd(map, stock_code, enable_amount,"7")){
//            return;
//        }
//        String risedown_rate = "2.0";
//        double nPrice = Double.parseDouble(newPrice);
//        String original_price = String.valueOf(nPrice+0.001);
//        risedownSell(stock_code, stock_name, original_price, risedown_rate, newPrice, enable_amount); //添加回落单
//    }
//
//    public void addStopProfitAndLoss(Map map,String stock_code, String stock_name, Integer enable_amount, String newPrice) throws Exception{
//        if (checkAddYmd(map, stock_code, enable_amount,"35")){
//            return;
//        }
//
//        String stop_loss_rate = "2.0"; //止
//
//        String stop_profit_rate = "100";
//        String stop_profit_price = getProfitPrice(newPrice, Double.parseDouble(stop_profit_rate));
//        String stop_loss_price = getLossPrice(newPrice, Double.parseDouble(stop_loss_rate));
//        stopProfitAndLoss(stock_code,stock_name,
//                newPrice,stop_profit_rate, stop_profit_price, stop_loss_rate, stop_loss_price, newPrice, enable_amount);
//
//    }
//
//    public void addHungSell(Map map,String stock_code, String stock_name, Integer enable_amount, String newPrice) throws Exception{
//        if (checkAddYmd(map, stock_code, enable_amount,"34")){
//            return;
//        }
//
//        String stop_loss_rate = "1"; //止
//        String original_price = getLossPrice(newPrice, Double.parseDouble(stop_loss_rate));
//        hungSell(stock_code,stock_name, original_price, newPrice, enable_amount);
//    }


//    public void addYmd(Map map,String stock_code, String stock_name, Integer enable_amount) throws Exception{
//        String newPrice = getNewPrice(stock_code); //获取最新
//        log.debug("-------------taskYmd------"+stock_code+"----"+enable_amount +"--" + newPrice);
//
//        addRisedownSell(map, stock_code, stock_name, enable_amount, newPrice);
//
//        addStopProfitAndLoss(map, stock_code, stock_name, enable_amount, newPrice);
//
//        addHungSell(map, stock_code, stock_name, enable_amount, newPrice);
//
//
//
//    }

    public String getLossPrice(String newPrice, double rate) {
        double nPrice = Double.parseDouble(newPrice);
        BigDecimal lossPrice = new BigDecimal(nPrice-(rate*nPrice/100));
        return String.valueOf(lossPrice.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
    }
    public String getProfitPrice(String newPrice, double rate) {
        double nPrice = Double.parseDouble(newPrice);
        BigDecimal profitPrice = new BigDecimal(nPrice+(rate*nPrice/100));
        return String.valueOf(profitPrice.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
     * 检查是否存在或不等
     * @param map
     * @param stock_code
     * @param enable_amount
     * @param strategy_id
     * @return
     * @throws Exception
     */
    public boolean checkAddYmd(Map<String, String> map, String stock_code, Integer enable_amount, String strategy_id, BigDecimal lossPrice) throws Exception{
        String key = appendStr(stock_code, strategy_id);
        if (map.containsKey(key)){
            String arr[] = map.get(key).split("_");
            if (arr[0].equals(String.valueOf(enable_amount))
                    && lossPrice.compareTo(new BigDecimal(arr[2]))==0){
                return true;
            }
            deleteYmd(arr[1]); //删除原条件单
        }
        return false;
    }

    /***
     * 删除 元条件单
     * @param map
     * @param stock_code
     * @param strategy_id
     * @return
     * @throws Exception
     */
    public boolean delYmd(Map<String, String> map, String stock_code, String strategy_id) throws Exception{
        String key = appendStr(stock_code, strategy_id);
        if (map.containsKey(key)){
            String arr[] = map.get(key).split("_");
            deleteYmd(arr[1]); //删除原条件单
        }
        return true;
    }

    /**
     *
     * @param str1
     * @param str2
     * @return
     */
    public String appendStr(String str1, String str2){
        StringBuffer stringBuffer = new StringBuffer(str1);
        return stringBuffer.append("_").append(str2).toString();
    }
}
