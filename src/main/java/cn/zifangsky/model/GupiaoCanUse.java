package cn.zifangsky.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "gupiao_can_use")
public class GupiaoCanUse implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 50)
    private String symbol;
    private Integer period;
    @Column(length = 50)
    private String bizDate;
    @Column(columnDefinition = "double(10,3) default '0.000'")
    private double lossPrice;
    private int stype;



}
