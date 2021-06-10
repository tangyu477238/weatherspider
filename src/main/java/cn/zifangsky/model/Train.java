package cn.zifangsky.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Train implements Serializable {
    private Long id;

    private String trainNo;
    private String trainNum;

    private String fromName;

    private String toName;
    
    private BigDecimal price;

    private BigDecimal num;

    private String fromTime;

    private String toTime;

    private String fromDate;

    private String toDate;


}