package cn.zifangsky.login;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URLEncoder;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoginTest {

    private String access_token="945e732c72cc4e519bda9655bb2e0b12";






    /***
     * deleteAllMyYmd
     * @throws Exception
     */
    @Test
    public void deleteAllMyYmd() throws Exception{

        Login.deleteAllMyYmd(access_token);

    }



    /***
     * 查询仓位
     * @throws Exception
     */
    @Test
    public void queryMyStockAmount() throws Exception{

        Login.queryMyStockAmount(access_token);

    }

    @Test
    public void gridYmd() throws Exception{
//        String access_token = "8b62a82e2f074d6c821557ee38334400";
        String stock_code = "159949";
        String stock_name = "创业板50";

//            String stock_code = "300008";
//            String stock_name = "天海防务";

//            String stock_code = "515210";
//            String stock_name = "钢铁ETF";
//

        String base_price  = "1.500";
        String lower_limit = "1.000";
        String upper_limit = "2.000";
        String increase = "1.00";
        String decrease = "1.00";
        String close_after_entrust_failure= "false";

        String current_price= "1.469";
        int position_upper_limit=10000;
        int position_lower_limit=1000;
        int entrust_amount = 1300; //委托数
        Login.gridYmd( stock_code,  stock_name,  base_price, lower_limit, upper_limit,increase, decrease, close_after_entrust_failure,
                current_price,  position_upper_limit,  position_lower_limit,  entrust_amount,  access_token);

    }

    /***
     * 均价突破
     */
    @Test
    public void avgLineYmd() throws Exception{

//        String access_token = "8b62a82e2f074d6c821557ee38334400";
        String stock_code = "600010";
        String stock_name = "包钢股份";
        int trigger_mode = 1;
        int avg_line = 60;
        int duration = 3;
        int entrust_bs = 1;

        String current_price = "1.60"; //当前价格(无效)public void avgLineYmd() throws Exception{
        int entrust_amount = 100; //委托数
        Login.avgLineYmd(stock_code, stock_name, trigger_mode,avg_line,duration,entrust_bs, current_price, entrust_amount, access_token);

    }

    /**
     *  删除 任务
     */
    @Test
    public void deleteYmd() throws Exception{
//        String access_token = "8b62a82e2f074d6c821557ee38334400";
        String ymd_id ="25842";
        Login.deleteYmd(ymd_id, access_token);
    }


    /****
     * 挂单买入

     */
    @Test
    public void hungBuy() throws Exception{
//        String access_token = "8b62a82e2f074d6c821557ee38334400";
//        String stock_code = "600004";
//        String stock_name = "白云机场";
//        String original_price = "9.00"; //触发价格
//        String current_price = "11.30"; //当前价格(无效)
//        int entrust_amount = 200; //委托数

        String stock_code = "300008";
        String stock_name = "天海防务";
        String original_price = "6.00"; //触发价格
        String current_price = "5.30"; //当前价格(无效)
        int entrust_amount = 100; //委托数

        Login.hungBuy(stock_code, stock_name, original_price, current_price, entrust_amount, access_token);
    }


    /****
     * 定价卖出

     */
    @Test
    public void hungSell() throws Exception{


        String stock_code = "159949";
        String stock_name = "创业板50";
        String original_price = "2.000"; //触发价格
        String current_price = "1.503"; //当前价格(无效)
        int entrust_amount = 100; //委托数

        Login.hungSell(stock_code, stock_name, original_price, current_price, entrust_amount, access_token);
    }

    /****
     * 止盈止损
     */
    @Test
    public void stopProfitAndLoss() throws Exception{



        String stock_code = "159949";
        String stock_name = "创业板50";
        String base_price = "1.503";

        String stop_profit_rate = "10.00"; //止盈
        String stop_loss_rate = "1.00"; //止损
        String stop_loss_price = "1.4880";
        String stop_profit_price = "1.6533";

        String current_price = "1.503";
        int entrust_amount = 100; //委托数

        Login.stopProfitAndLoss(stock_code, stock_name, base_price,stop_profit_rate, stop_loss_rate, stop_loss_price, stop_profit_price,  current_price, entrust_amount, access_token);
    }

}
