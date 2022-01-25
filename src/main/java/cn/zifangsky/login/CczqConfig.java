package cn.zifangsky.login;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
@Data
public class CczqConfig {

    private String compId = "1091";

    @Value("${cczq.hs_openid}")
    private String hs_openid ;
    @Value("${cczq.fund_account}")
    private String fund_account ;
    @Value("${cczq.h_stock_account}")
    private String h_stock_account ;
    @Value("${cczq.s_stock_account}")
    private String s_stock_account ;

//    @Value("${cczq.access_token}")
//    private String access_token;
}
