package cn.zifangsky.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Entity
@Table(name = "gupiao_kline_5m")
public class GupiaoKline5m  extends  BaseGupiaoKline {
    private static final long serialVersionUID = -5395611529404702931L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
