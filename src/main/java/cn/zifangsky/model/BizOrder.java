package cn.zifangsky.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@Entity
@Table(name = "biz_order")
public class BizOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("create_time")
    @Column(length = 50)
    private String createTime;

    @JsonProperty("biz_date")
    @Column(length = 50)
    private String bizDate;

    @JsonProperty("order_no")
    @Column(length = 50)
    private String orderNo;

    @JsonProperty("route_name")
    @Column(length = 200)
    private String routeName;

    @JsonProperty("info")
    @Column(length = 200)
    private String info;

    @JsonProperty("num")
    private BigDecimal num;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("name")
    @Column(length = 50)
    private String name;

    @JsonProperty("mobile")
    @Column(length = 50)
    private String mobile;

    @JsonProperty("state")
    @Column(length = 50)
    private String state;

    @JsonProperty("plat_form")
    @Column(length = 50)
    private String platForm;

}
