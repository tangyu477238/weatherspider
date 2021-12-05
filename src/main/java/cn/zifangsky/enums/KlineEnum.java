package cn.zifangsky.enums;


public enum KlineEnum {

    K_1M(1,"1","1分钟"),
    K_5M(5,"5","5分钟"),
    K_15M(15,"15","15分钟"),
    K_30M(30,"30","30分钟"),
    K_60M(60,"60","60分钟"),
    K_120M(120,"120","120分钟"),
    K_1D(101,"101","1天"),
    K_1W(102,"102","1周"),
    K_1MON(3,"","1月"),
    K_1YEAR(3,"","1年"),
    ;



    private final int id;
    private final String code;
    private final String name;

    KlineEnum(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }





}
