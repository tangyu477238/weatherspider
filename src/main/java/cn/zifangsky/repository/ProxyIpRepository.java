package cn.zifangsky.repository;

import cn.zifangsky.model.ProxyIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProxyIpRepository extends JpaRepository<ProxyIp, Long> {

    ProxyIp findByIpAndPort(String ip, Integer port);

    @Query(value = "select * from proxyip where update_time is not null", nativeQuery = true)
    List<ProxyIp> listCanUse();
}
