package cn.zifangsky.manager;

import cn.zifangsky.model.ProxyIp;
import cn.zifangsky.model.bo.ProxyIpBO;

import java.util.List;

public interface ProxyIpManager {

    int deleteByPrimaryKey(Long id);

    void deleteByProxy(ProxyIp proxyIp);

    void insertCheckProxy(ProxyIp proxyIp);

    ProxyIp updateCheckProxy(ProxyIp proxyIp);

    ProxyIp updateProxy(ProxyIp proxyIp);


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

    List<ProxyIp> selectCanUseALL();
	/**
	 * 从代理IP池的前10条数据中任意返回一条给用户
	 * @return
	 */
    ProxyIp selectCheckRandomIP();

    ProxyIp selectRandomIP();

    void addProxy(ProxyIpBO proxyIpBO);

    void addProxyAll(List<ProxyIp> list);


}
