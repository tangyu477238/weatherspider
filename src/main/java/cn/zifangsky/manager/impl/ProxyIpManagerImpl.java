package cn.zifangsky.manager.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.repository.ProxyIpRepository;
import cn.zifangsky.spider.CheckIPUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.ProxyIp;

import javax.annotation.Resource;

@Service("proxyIpManager")
public class ProxyIpManagerImpl implements ProxyIpManager {


	@Resource(name = "proxyIpRepository")
	private ProxyIpRepository proxyIpRepository;


	@Override
	public List<ProxyIp> selectAll() {
		return proxyIpRepository.findAll();
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		proxyIpRepository.deleteById(id);
		return 1;
	}

	@Override
	public int insert(ProxyIp proxyIp) {

		ProxyIp proxy = new ProxyIp();
		BeanUtils.copyProperties(proxyIp, proxy);
		proxy.setUpdateTime(new Date());
		proxyIpRepository.save(proxy);

		return 1;
	}

	@Override
	public ProxyIp selectByIPPort(String ip, Integer port) {
		return proxyIpRepository.findByIpAndPort(ip, port);
	}

	@Override
	public ProxyIp selectRandomIP() {
		List<ProxyIp> list = selectAll();
		if(ComUtil.isEmpty(list)){
			return null;
		}
		Collections.shuffle(list);
		return list.get(0);
	}

	@Override
	public ProxyIp selectCheckRandomIP() {
		ProxyIp proxyIp = selectRandomIP();
		if (ComUtil.isEmpty(proxyIp)){
			return null;
		}
		if (CheckIPUtils.checkValidIP(proxyIp.getIp(), proxyIp.getPort())) {
			return proxyIp;
		}
		try {deleteByPrimaryKey(proxyIp.getId());}catch (Exception e){}
		return selectCheckRandomIP();
	}
}
