package cn.zifangsky.enums;

public enum DongfangEnum {

    KLINE("KLINE",".push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery112403780605306048155_1618930055627&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&ut=7eea3edcaed734bea9cbfc24409ed989"),

   ;


    private final String code;
    private final String url;

    DongfangEnum(String code, String url) {
        this.code = code;
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public String getCode() {
        return code;
    }


    public static String getDongfangUrl(String code) {
        for (DongfangEnum dongfangEnum : DongfangEnum.values()) {
            if (dongfangEnum.getCode().equals(code)){
                return dongfangEnum.getUrl();
            }
        }
        return null;
    }
}
