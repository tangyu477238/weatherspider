package cn.zifangsky.login;

public class StockUtil {
    public static boolean isShenshi(String stock_code){
        return !stock_code.startsWith("60") && !stock_code.startsWith("11") && !stock_code.startsWith("51");
    }
}
