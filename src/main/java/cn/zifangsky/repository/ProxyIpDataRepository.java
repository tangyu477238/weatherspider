package cn.zifangsky.repository;

import cn.zifangsky.model.ProxyIpData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProxyIpDataRepository extends JpaRepository<ProxyIpData, Long> {

    ProxyIpData findByIpAndPort(String ip, Integer port);


}
