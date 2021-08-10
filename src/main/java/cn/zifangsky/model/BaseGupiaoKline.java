package cn.zifangsky.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
public class BaseGupiaoKline implements Serializable {
    private Integer id;
    private String symbol;
    private String period;

    @JsonProperty("biz_date")
    private String bizDate;
    private Date timestamp;
    private double volume;
    private double open;
    private double high;
    private double low;
    private double close;
    private double chg; //
    private double percent;
    private double turnoverrate;
    private String amount;

    @JsonProperty("volume_post")
    private String volumePost;
    @JsonProperty("amount_post")
    private String amountPost;
    private String pe;
    private String pb;
    private String ps;
    private String pcf;
    @JsonProperty("market_capital")
    private String marketCapital;
    private String balance;
    @JsonProperty("hold_volume_cn")
    private String holdVolumeCn;
    @JsonProperty("hold_ratio_cn")
    private String holdRatioCn;
    @JsonProperty("net_volume_cn")
    private String netVolumeCn;
    @JsonProperty("hold_volume_hk")
    private String holdVolumeHk;
    @JsonProperty("hold_ratio_hk")
    private String holdRatioHk;
    @JsonProperty("net_volume_hk")
    private String netVolumeHk;
}
