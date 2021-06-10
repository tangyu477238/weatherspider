package cn.zifangsky.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "xueqiu_gupiao_gainian")
public class XueqiuGupiaoGainian implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String symbol;
    private String gainian;

}
