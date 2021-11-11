package cn.zifangsky.login;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CybManager{

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

    @Resource
    private LoginManager loginManager;

    /****
     * 补仓
     * @return
     * @throws Exception
     */
    public int buchongStockNum () throws Exception{
        String stock_code = "159949";
        Integer enableAmount = loginManager.getEnableAmount( stock_code);
        Integer currentAmount = loginManager.getCurrentAmount( stock_code);
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
    public Double getNewPriceCyb() {
        String stock_code = "159949";
        String stock_name = "创业板50";
        Double newPrice = Double.parseDouble(loginManager.getNewPrice(stock_code));
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

        loginManager.hungSell(stock_code, stock_name, original_price, current_price, entrust_amount );
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

        loginManager.hungBuy(stock_code, stock_name, original_price, current_price, entrust_amount );
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
        String json = loginManager.gridYmd( stock_code,  stock_name,  base_price, lower_limit, upper_limit,increase, decrease, close_after_entrust_failure,
                current_price,  position_upper_limit,  position_lower_limit,  entrust_amount );
        return JSONUtil.parseObj(json).getStr("data");
    }
}
