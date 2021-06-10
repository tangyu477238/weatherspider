package cn.zifangsky.repository;

import cn.zifangsky.model.ProxyIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProxyIpRepository extends JpaRepository<ProxyIp,Long> {

    ProxyIp findByIpAndPort(String ip, Integer port);

//    ProxyIp findByBondId(String bondId);
}
