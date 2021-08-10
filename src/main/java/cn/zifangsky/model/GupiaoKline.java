package cn.zifangsky.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "gupiao_kline")
public class GupiaoKline extends BaseGupiaoKline{
    private static final long serialVersionUID = -5395611529404702931L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

}
