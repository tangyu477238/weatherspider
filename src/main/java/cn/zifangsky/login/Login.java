package cn.zifangsky.login;

import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;

@Slf4j
public class Login {

    static String comp_id = "1091";
    static String hs_openid = "10918505173";
    static String fund_account = "8505173";
    static String h_stock_account = "A135314381";
    static String s_stock_account = "0268411912";


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
     * @param access_token
     * @return
     * @throws Exception
     */
    public static String gridYmd(String stock_code, String stock_name, String base_price,String lower_limit,String upper_limit,
                                  String increase,String decrease,String close_after_entrust_failure,
                                  String current_price, int position_upper_limit, int position_lower_limit, int entrust_amount, String access_token) throws Exception{

        int exchange_type = (!stock_code.startsWith("60") && !stock_code.startsWith("11") && !stock_code.startsWith("51"))  ? 2 : 1; //深/沪
        int cep_type = 1;

        int entrust_price_type = 1; //挂单买入
        String entrust_price_mode = "NewPrice,NewPrice"; //即时价格
        entrust_price_mode = URLEncoder.encode(entrust_price_mode, "UTF-8");
        String expiry_days = "2021-07-27 15:00:00"; //失效日期+时间
        expiry_days = URLEncoder.encode(expiry_days, "UTF-8");
        expiry_days = expiry_days.replaceAll("\\+", "%20");
        stock_name = URLEncoder.encode(stock_name, "UTF-8");

        String stock_account = (!stock_code.startsWith("60") && !stock_code.startsWith("11") && !stock_code.startsWith("51"))? s_stock_account : h_stock_account; //沪市或深市
        String op_station = "MA;IIP:111.196.241.22;IPORT:NA;LIP:192.168.16.10;MAC:5CC307738AE8;IMEI:NA;RMPN:13552379492;UMPN:+8613552379492;ICCID:NA;OSV:ANDROID10;IMSI:NA@TDXADR;V5.00;HSTJD";
        op_station = URLEncoder.encode(op_station, "UTF-8");
        String ext = "{\"channel\":null}";
        ext = URLEncoder.encode(ext, "UTF-8");
        String url = "https://tjd.cczq.com:5000/cczq/biz/v/gridYmd?stock_code="+stock_code+"&stock_name="+stock_name
                +"&base_price="+base_price+"&lower_limit="+lower_limit +"&upper_limit="+upper_limit+"&increase="+increase
                +"&decrease="+decrease+"&close_after_entrust_failure="+close_after_entrust_failure
                +"&exchange_type="+exchange_type+"&current_price="+current_price+"&cep_type="+cep_type
                +"&position_upper_limit="+position_upper_limit+"&position_lower_limit="+position_lower_limit+"&expiry_days="+expiry_days
                +"&entrust_price_type="+entrust_price_type+"&entrust_amount="+entrust_amount
                +"&stock_account="+stock_account+"&entrust_price_mode="+entrust_price_mode+"&comp_id="+comp_id+"&hs_openid="+hs_openid
                +"&access_token="+access_token+"&fund_account="+fund_account+"&op_station="+op_station+"&ext="+ext;
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
     * @param access_token
     * @return
     * @throws Exception
     */
    public static String avgLineYmd(String stock_code, String stock_name, int trigger_mode,int avg_line,int duration,int entrust_bs,
                                     String current_price, int entrust_amount, String access_token) throws Exception{

        int exchange_type = Double.parseDouble(current_price) > 0 ? 1 : 2; //下探1或上穿2
        int cep_type = 1;

        int entrust_price_type = 1; //挂单买入
        String entrust_price_mode = "NewPrice"; //即时价格

        String expiry_days = "2021-07-26 15:00:00"; //失效日期+时间
        expiry_days = URLEncoder.encode(expiry_days, "UTF-8");
        expiry_days = expiry_days.replaceAll("\\+", "%20");
        String expiry_days_text = "";
        stock_name = URLEncoder.encode(stock_name, "UTF-8");

        String stock_account = (!stock_code.startsWith("60") && !stock_code.startsWith("11") && !stock_code.startsWith("51"))? s_stock_account : h_stock_account; //沪市或深市
        String op_station = "MA;IIP:111.196.241.22;IPORT:NA;LIP:192.168.16.10;MAC:5CC307738AE8;IMEI:NA;RMPN:13552379492;UMPN:+8613552379492;ICCID:NA;OSV:ANDROID10;IMSI:NA@TDXADR;V5.00;HSTJD";
        op_station = URLEncoder.encode(op_station, "UTF-8");
        String ext = "{\"channel\":null}";
        ext = URLEncoder.encode(ext, "UTF-8");
        String url = "https://tjd.cczq.com:5000/cczq/biz/v/avgLineYmd?stock_code="+stock_code+"&stock_name="+stock_name
                +"&trigger_mode="+trigger_mode+"&avg_line="+avg_line
                +"&duration="+duration+"&entrust_bs="+entrust_bs
                +"&current_price="+current_price+"&exchange_type="+exchange_type+"&cep_type="+cep_type+"&expiry_days="+expiry_days
                +"&expiry_days_text="+expiry_days_text+"&entrust_price_type="+entrust_price_type+"&entrust_amount="+entrust_amount
                +"&stock_account="+stock_account+"&entrust_price_mode="+entrust_price_mode+"&comp_id="+comp_id+"&hs_openid="+hs_openid
                +"&access_token="+access_token+"&fund_account="+fund_account+"&op_station="+op_station+"&ext="+ext;
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
     * @param access_token
     * @return
     * @throws Exception
     */
    public static String deleteYmd(String ymd_id, String access_token) throws Exception{

        String op_station = "MA;IIP:111.196.241.22;IPORT:NA;LIP:192.168.16.10;MAC:5CC307738AE8;IMEI:NA;RMPN:13552379492;UMPN:+8613552379492;ICCID:NA;OSV:ANDROID10;IMSI:NA@TDXADR;V5.00;HSTJD";
        op_station = URLEncoder.encode(op_station, "UTF-8");
        String ext = "{\"channel\":null}";
        ext = URLEncoder.encode(ext, "UTF-8");
        String url = "https://tjd.cczq.com:5000/cczq/biz/v/deleteYmd?ymd_id="+ymd_id+"&is_delete=0"
                +"&comp_id="+comp_id+"&hs_openid="+hs_openid
                +"&access_token="+access_token+"&fund_account="+fund_account+"&op_station="+op_station+"&ext="+ext;

        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
            log.info(httpOrgCreateTestRtn);
            return httpOrgCreateTestRtn;
    }


    /****
     * 挂单买入
     * @param stock_code
     * @param stock_name
     * @param original_price
     * @param current_price
     * @param entrust_amount
     * @param access_token
     * @return
     * @throws Exception
     */
    public static String hungBuy(String stock_code, String stock_name, String original_price, String current_price,
                                  int entrust_amount, String access_token) throws Exception{

        int exchange_type = Double.parseDouble(original_price) < Double.parseDouble(current_price) ? 1 : 2; //下探1或上穿2
        int cep_type = 1;

        int entrust_price_type = 1; //挂单买入
        String entrust_price_mode = "NewPrice"; //即时价格

        String expiry_days = "2021-07-26 15:00:00"; //失效日期+时间
        expiry_days = URLEncoder.encode(expiry_days, "UTF-8");
        expiry_days = expiry_days.replaceAll("\\+", "%20");
        String expiry_days_text = "";
        stock_name = URLEncoder.encode(stock_name, "UTF-8");

        String stock_account = (!stock_code.startsWith("60") && !stock_code.startsWith("11") && !stock_code.startsWith("51"))? s_stock_account : h_stock_account; //沪市或深市
        String op_station = "MA;IIP:111.196.241.22;IPORT:NA;LIP:192.168.16.10;MAC:5CC307738AE8;IMEI:NA;RMPN:13552379492;UMPN:+8613552379492;ICCID:NA;OSV:ANDROID10;IMSI:NA@TDXADR;V5.00;HSTJD";
        op_station = URLEncoder.encode(op_station, "UTF-8");
        String ext = "{\"channel\":null}";
        ext = URLEncoder.encode(ext, "UTF-8");
        String url = "https://tjd.cczq.com:5000/cczq/biz/v/hungBuy?stock_code="+stock_code
                +"&stock_name="+stock_name+"&original_price="+original_price+"&exchange_type="+exchange_type
                +"&current_price="+current_price+"&cep_type="+cep_type+"&expiry_days="+expiry_days
                +"&expiry_days_text="+expiry_days_text+"&entrust_price_type="+entrust_price_type+"&entrust_amount="+entrust_amount
                +"&stock_account="+stock_account+"&entrust_price_mode="+entrust_price_mode+"&comp_id="+comp_id+"&hs_openid="+hs_openid
                +"&access_token="+access_token+"&fund_account="+fund_account+"&op_station="+op_station+"&ext="+ext;
        log.info("");
        log.info(url);
        log.info("");
        String httpOrgCreateTestRtn = HttpClientUtil.get(url);
        log.info(httpOrgCreateTestRtn);
        return httpOrgCreateTestRtn;
    }





}
