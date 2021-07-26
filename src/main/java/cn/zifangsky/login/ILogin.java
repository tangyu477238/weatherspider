package cn.zifangsky.login;

public interface ILogin {

//    检查是否已经存在条件单
    boolean checkMyYmd(String stock_code, String strategy_id) throws Exception;

    //生成回落单
    void addRisedownYmd(String stock_code, String stock_name, Integer enable_amount, String decline_rate) throws Exception;

}
