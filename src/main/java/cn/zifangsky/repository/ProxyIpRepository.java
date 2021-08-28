package cn.zifangsky.repository;

import cn.zifangsky.model.ProxyIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProxyIpRepository extends JpaRepository<ProxyIp, Long> {

    ProxyIp findByIpAndPort(String ip, Integer port);

    @Query(value = "select * from proxyip where update_time is not null ORDER BY update_time DESC LIMIT ?1,3", nativeQuery = true)
    List<ProxyIp> listCanUse(Integer st0);

    @Query(value = "select * from proxyip order by update_time  LIMIT 0,20", nativeQuery = true)
    List<ProxyIp> listProxy();
}
