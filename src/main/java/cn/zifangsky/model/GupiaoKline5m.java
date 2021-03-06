package cn.zifangsky.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "gupiao_kline_5m")
public class GupiaoKline5m  extends  BaseGupiaoKline {
    private static final long serialVersionUID = -5395611529404702931L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String symbol;
    private Integer period;

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
