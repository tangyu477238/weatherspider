package cn.zifangsky.manager.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.model.bo.ProxyIpBO;
import cn.zifangsky.repository.ProxyIpRepository;
import cn.zifangsky.spider.CheckIPUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.ProxyIp;

import javax.annotation.Resource;

@Service("proxyIpManager")
@Slf4j
@Data
public class ProxyIpManagerImpl implements ProxyIpManager {

	private boolean isDel = false;

	private Map<String, ProxyIp> map = new ConcurrentHashMap<>();

	@Resource
	private ProxyIpRepository proxyIpRepository;

	@Resource
	private CheckIPUtils checkIPUtils;

	@Override
	public List<ProxyIp> selectAll() {
		return proxyIpRepository.findAll();
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
//		ProxyIp proxyIp = proxyIpRepository.getOne(id);
//		proxyIp.setUsed(proxyIp.getUsed()+1);
//		if (proxyIp.getUsed()>100){
			proxyIpRepository.deleteById(id);
//		} else {
//			proxyIpRepository.save(proxyIp);
//		}
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
		return getProxy(false);
	}

	@Override
	public ProxyIp selectCheckRandomIP() {
		return getProxy(true);
	}


	private ProxyIp getProxy(boolean isDel){
		List<ProxyIp> list = selectAll();
		if(ComUtil.isEmpty(list)){
			synProxy(); //废弃IP重存
			return null;
		}
		Collections.shuffle(list);
		for (ProxyIp proxyIp : list){
			if (checkIPUtils.checkValidIP(proxyIp.getIp(), proxyIp.getPort())) {
				return proxyIp;
			}
			try {
				if (isDel){
					deleteByPrimaryKey(proxyIp.getId());
				}
			}catch (Exception e){}
		}
		return null;
	}

	private synchronized void synProxy(){
		if (map.size()<=0){
			return;
		}
		List<ProxyIp> list = new ArrayList<>();
		log.info("---------废弃IP重新利用----开始-----"+ DateUtil.formatAsDatetime(new Date()));
		ProxyIp proxyIp;
		for (ProxyIp proxyIp1 : map.values()) {
			proxyIp = new ProxyIp();
			BeanUtils.copyProperties(proxyIp1, proxyIp);
			list.add(proxyIp);
		}
		proxyIpRepository.saveAll(list);
		log.info(list.size()+"---------废弃IP重新利用----结束---"+ DateUtil.formatAsDatetime(new Date()));
	}



	@Override
	public void addPropx(ProxyIpBO proxyIpBO) {
		try {
			if (map.containsKey(proxyIpBO.getIp()+proxyIpBO.getPort())){
				if (map.size()>200000){
					log.info("--------map:达到了20万,开始减半--------");
					map.forEach((k,v) -> {
						if (map.size()<100000){
							log.info("--------map:减半至10万-------");
							return;
						}
						map.remove(k);
					});
				}
				return;
			}
			map.put(proxyIpBO.getIp()+proxyIpBO.getPort(), proxyIpBO); //存进集合
			// 根据该IP是待入库的新IP或者数据库中的旧IP分两种情况判断
			if (proxyIpBO.getCheckType() == ProxyIpBO.CheckIPType.ADD) {
				// 1 查询该IP是否已存在
				ProxyIp oldIP = selectByIPPort(proxyIpBO.getIp(), proxyIpBO.getPort());
				if (oldIP != null) {
					return ;
				}
				if (!checkIPUtils.checkValidIP(proxyIpBO.getIp(), proxyIpBO.getPort())) {
					return ;
				}
				insert(proxyIpBO);
			}
			if (proxyIpBO.getCheckType() == ProxyIpBO.CheckIPType.UPDATE) {
				log.debug("检查ip的有效性");
				if (checkIPUtils.checkValidIP(proxyIpBO.getIp(), proxyIpBO.getPort())) {
					return ;
				}
				deleteByPrimaryKey(proxyIpBO.getId()); // 不能使用则删除
			}
		} catch (Exception e) {
			log.debug("");
		}
	}
}
