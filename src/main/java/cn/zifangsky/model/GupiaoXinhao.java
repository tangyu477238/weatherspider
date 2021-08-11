package cn.zifangsky.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@Entity
@Table(name = "gupiao_xinhao")
public class GupiaoXinhao implements Serializable {
    private static final long serialVersionUID = -5395611529404702931L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("symbol")
    @Column(length = 50)
    private String symbol;

    @JsonProperty("name")
    @Column(length = 50)
    private String name;

    @JsonProperty("type")
    @Column(length = 10)
    private int type;

    @JsonProperty("type_name")
    @Column(length = 10)
    private String typeName;

    @JsonProperty("biz_date")
    @Column(length = 50)
    private String bizDate;

    @JsonProperty("sj1")
    @Column(precision = 20, scale = 2)
    private BigDecimal sj1;

    @JsonProperty("sj2")
    @Column(precision = 20, scale = 2)
    private BigDecimal sj2;

    @JsonProperty("sj3")
    @Column(precision = 20, scale = 2)
    private BigDecimal sj3;

    @JsonProperty("sj4")
    @Column(precision = 20, scale = 2)
    private BigDecimal sj4;

    @JsonProperty("sj5")
    @Column(precision = 20, scale = 2)
    private BigDecimal sj5;



    @JsonProperty("sj6")
    @Column(precision = 20, scale = 2)
    private BigDecimal sj6;

    @JsonProperty("sj7")
    @Column(precision = 20, scale = 2)
    private BigDecimal sj7;

    @JsonProperty("sj8")
    @Column(precision = 20, scale = 2)
    private BigDecimal sj8;

    @JsonProperty("sj9")
    @Column(precision = 20, scale = 2)
    private BigDecimal sj9;

    @JsonProperty("sj10")
    @Column(precision = 20, scale = 2)
    private BigDecimal sj10;

}
