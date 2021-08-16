package cn.zifangsky.manager;

import cn.zifangsky.model.ProxyIp;

import java.util.List;

public interface ProxyIpManager {

    int deleteByPrimaryKey(Long id);

    int insert(ProxyIp proxyIp);

    /**
     * 根据IP和端口查询
     * @param ip
     * @param port
     * @return
     */
    ProxyIp selectByIPPort(String ip,Integer port);
	/**
     * 无条件查询所有
     * @return
     */
    List<ProxyIp> selectAll();
    
	/**
	 * 从代理IP池的前10条数据中任意返回一条给用户
	 * @return
	 */
    ProxyIp selectCheckRandomIP();

    ProxyIp selectRandomIP();
}
