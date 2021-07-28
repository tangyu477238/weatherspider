package cn.zifangsky.login;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoginManagerTest {


    @Resource
    private LoginManager loginManager;



    /***
     * deleteAllMyYmd
     * @throws Exception
     */
    @Test
    public void deleteAllMyYmd() throws Exception{

        loginManager.deleteAllMyYmd();

    }

    /***
     * 查询指定产品可用仓位
     * @throws Exception
     */
    @Test
    public void queryStockEnablelNum() throws Exception{
        String stock_code = "159949";
        loginManager.queryStockEnablelNum(stock_code);

    }

    /***
     * 查询所有产品仓位
     * @throws Exception
     */
    @Test
    public void queryMyStockAmount() throws Exception{
        String json = loginManager.queryMyStockAmount ();
        log.info(json);
    }

    /***
     * 添加回落条件单
     * @throws Exception
     */
    @Test
    public void addRisedownYmd() throws Exception{

        String json = loginManager.queryMyStockAmount ();
        JSONArray jsonArray = JSONUtil.parseObj(json).getJSONArray("data");
        for (Object object : jsonArray){
            JSONObject jsonObject = (JSONObject)object;
            Integer enable_amount = jsonObject.getInt("enable_amount");
            String stock_code = jsonObject.getStr("stock_code");
            String stock_name = jsonObject.getStr("stock_name");
            if (enable_amount>0){
//                loginManager.addRisedownYmd(stock_code, stock_name, enable_amount,"1");
            }
        }
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
        loginManager.gridYmd( stock_code,  stock_name,  base_price, lower_limit, upper_limit,increase, decrease, close_after_entrust_failure,
                current_price,  position_upper_limit,  position_lower_limit,  entrust_amount);

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
        loginManager.avgLineYmd(stock_code, stock_name, trigger_mode,avg_line,duration,entrust_bs, current_price, entrust_amount);

    }

    /**
     *  删除 任务
     */
    @Test
    public void deleteYmd() throws Exception{
//        String access_token = "8b62a82e2f074d6c821557ee38334400";
        String ymd_id ="25842";
        loginManager.deleteYmd(ymd_id);
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

        loginManager.hungBuy(stock_code, stock_name, original_price, current_price, entrust_amount);
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

        loginManager.hungSell(stock_code, stock_name, original_price, current_price, entrust_amount);
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

        loginManager.stopProfitAndLoss(stock_code, stock_name, base_price,stop_profit_rate, stop_loss_rate, stop_loss_price, stop_profit_price,  current_price, entrust_amount);
    }

}
