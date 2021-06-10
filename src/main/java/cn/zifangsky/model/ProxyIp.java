package cn.zifangsky.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "proxyip")
public class ProxyIp implements Serializable{
	private static final long serialVersionUID = -3699072211264713025L;

    public ProxyIp() {
    }

    public ProxyIp(Long id, String ip, Integer port, String type, String addr, Boolean used, String other) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.addr = addr;
        this.used = used;
        this.other = other;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;

    private Integer port;

    private String type;

    private String addr;

    private Boolean used;

    private String other;

    private Date updateTime;

}