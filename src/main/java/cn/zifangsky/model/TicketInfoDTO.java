package cn.zifangsky.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 车票信息
 * Create by Kalvin on 2019/9/19.
 */
@Data
public class TicketInfoDTO implements Serializable {

    private boolean onSale; // 是否开售
    private String secretStr;   // 密钥串
    private String trainNo;     // 列车号
    private String trainNum;    // 车次
    private String formStationName; // 始发站名称
    private String toStationName; // 终点站名称
    private String formStationTelecode; // 12306始发站代码
    private String toStationTelecode;   // 12306终点站代码
    private String goOffTime;   // 出发时间
    private String arrivalTime; // 到达时间
    private String lastTime;    // 历时时间
    private String leftTicket;
    private String trainLocation;
    private String bizDate; //日期

    private String businessSeat;    // 商务特等座
    private String l1Seat;  // 一等座
    private String l2Seat;  // 二等座
    private String l1SoftBerth; // 软卧一等卧
    private String l2HardBerth; // 硬卧二等卧
    private String hardSeat;    // 硬座
    private String softSeat;    // 软座
    private String noSeat;      // 无座
    private boolean canAlternate;   // 是否可候补。1代表能候补
    private String canNotAlternateSeatType; // 不能候补的座席

}
