package cn.zifangsky.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TrainInfo implements Serializable {
    private Long id;

    private String trainNo;
    private String trainNum;
    private String stationName;
    private BigDecimal price;
    private BigDecimal num;
    private String arriveTime;
    private String startTime;
    private String biz_date;
    private String sort;


}