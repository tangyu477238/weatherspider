package cn.zifangsky.enums;


public enum ProxyUrlEnum {

    PROXY_1(1,"https://www.kuaidaili.com/free/inha/1/","cn.zifangsky.spider.ProxyIPSpider9"),
    PROXY_11(2,"https://www.kuaidaili.com/free/intr/1/","cn.zifangsky.spider.ProxyIPSpider9"),

    PROXY_2(3,"http://www.ip3366.net/?stype=1&page=1","cn.zifangsky.spider.ProxyIPSpider6"),
    PROXY_20(3,"http://www.ip3366.net/?stype=2&page=1","cn.zifangsky.spider.ProxyIPSpider6"),
    PROXY_21(3,"http://www.ip3366.net/?stype=3&page=1","cn.zifangsky.spider.ProxyIPSpider6"),
    PROXY_22(3,"http://www.ip3366.net/?stype=4&page=1","cn.zifangsky.spider.ProxyIPSpider6"),
    PROXY_23(3,"http://www.ip3366.net/?stype=5&page=1","cn.zifangsky.spider.ProxyIPSpider6"),

    PROXY_3(3,"https://ip.jiangxianli.com/?page=1","cn.zifangsky.spider.ProxyIPSpider"),
    PROXY_31(3,"https://ip.jiangxianli.com/?page=2","cn.zifangsky.spider.ProxyIPSpider"),
    PROXY_32(3,"https://ip.jiangxianli.com/?page=3","cn.zifangsky.spider.ProxyIPSpider"),
    PROXY_33(3,"https://ip.jiangxianli.com/?page=4","cn.zifangsky.spider.ProxyIPSpider"),

    PROXY_4(3,"http://www.xiladaili.com/","cn.zifangsky.spider.ProxyIPSpider5"),
    PROXY_41(3,"http://www.xiladaili.com/putong/","cn.zifangsky.spider.ProxyIPSpider5"),
    PROXY_42(3,"http://www.xiladaili.com/gaoni/","cn.zifangsky.spider.ProxyIPSpider5"),
    PROXY_43(3,"http://www.xiladaili.com/https/","cn.zifangsky.spider.ProxyIPSpider5"),
    PROXY_44(3,"http://www.xiladaili.com/http/","cn.zifangsky.spider.ProxyIPSpider5"),


    PROXY_7(3,"https://list.proxylistplus.com/Socks-List-1","cn.zifangsky.spider.ProxyIPSpider3"),
    PROXY_71(3,"https://list.proxylistplus.com/Fresh-HTTP-Proxy-List-1","cn.zifangsky.spider.ProxyIPSpider3"),
    PROXY_72(3,"https://list.proxylistplus.com/SSL-List-1","cn.zifangsky.spider.ProxyIPSpider3"),
    PROXY_73(3,"https://list.proxylistplus.com/Google","cn.zifangsky.spider.ProxyIPSpider3"),

    PROXY_8(3,"http://www.89ip.cn/index_1.html","cn.zifangsky.spider.ProxyIPSpider2"),
    PROXY_80(3,"http://www.89ip.cn/index_2.html","cn.zifangsky.spider.ProxyIPSpider2"),
    PROXY_81(3,"http://www.89ip.cn/index_3.html","cn.zifangsky.spider.ProxyIPSpider2"),
    PROXY_82(3,"http://www.89ip.cn/index_4.html","cn.zifangsky.spider.ProxyIPSpider2"),
    PROXY_83(3,"http://www.89ip.cn/index_5.html","cn.zifangsky.spider.ProxyIPSpider2"),

    PROXY_9(3,"https://ip.ihuan.me/","cn.zifangsky.spider.ProxyIPSpider4"),
    PROXY_90(3,"https://ip.ihuan.me/","cn.zifangsky.spider.ProxyIPSpider4"),
    PROXY_91(3,"https://ip.ihuan.me/","cn.zifangsky.spider.ProxyIPSpider4"),
    PROXY_92(3,"https://ip.ihuan.me/","cn.zifangsky.spider.ProxyIPSpider4"),





    ;



    private final int id;
    private final String code;
    private final String name;

    ProxyUrlEnum(int id, String code, String name) {
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



    public static String getProxyUrl(String code) {
        for (ProxyUrlEnum proxyUrlEnum : ProxyUrlEnum.values()) {
            if (proxyUrlEnum.getCode().equals(code)){
                return proxyUrlEnum.getName();
            }
        }
        return ProxyUrlEnum.PROXY_1.getName();
    }



}
