package cn.zifangsky.manager.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.model.ProxyIpData;
import cn.zifangsky.model.bo.ProxyIpBO;
import cn.zifangsky.repository.ProxyIpDataRepository;
import cn.zifangsky.repository.ProxyIpRepository;
import cn.zifangsky.common.CheckIPUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
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
	private ProxyIpDataRepository proxyIpDataRepository;
	@Resource
	private ProxyIpRepository proxyIpRepository;

	@Resource
	private CheckIPUtils checkIPUtils;

	@Override
	public List<ProxyIp> selectAll() { //未验证ip
		List<ProxyIp> list = proxyIpRepository.listProxy();
		if (ComUtil.isEmpty(list)){
			synProxy(); //废弃IP重存
		}
		return list;
	}

	@Override
	public List<ProxyIp> selectCanUseALL() {
		List<ProxyIp> list = proxyIpRepository.listCanUse();
		if (!ComUtil.isEmpty(list)){
			Collections.shuffle(list);
			return list;
		}
		return selectAll();
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
	public boolean insert(ProxyIpBO proxyIpBO) {
		// 0 查询该IP是否 验证过
		if (map.containsKey(proxyIpBO.getIp()+proxyIpBO.getPort())){
			return false;
		}
		map.put(proxyIpBO.getIp()+proxyIpBO.getPort(), proxyIpBO); //存进集合

		// 1 查询该IP 是否已存在
		ProxyIp oldIP = selectByIPPort(proxyIpBO.getIp(), proxyIpBO.getPort());
		if (!ComUtil.isEmpty(oldIP)) {
			return false;
		}
		if (!checkIPUtils.checkValidIP(proxyIpBO.getIp(), proxyIpBO.getPort())) {
			return false;
		}

		ProxyIp proxy = new ProxyIp();
		BeanUtils.copyProperties(proxyIpBO, proxy);
		proxy.setUpdateTime(new Date());
		proxyIpRepository.save(proxy);
		log.info("---收录成功---"+proxyIpBO.getIp() +":"+ proxyIpBO.getPort());
		addProxyData(proxyIpBO); //记录ip

		return true;
	}

	private void addProxyData(ProxyIpBO proxyIpBO){
		try {
			ProxyIpData proxyIpData = proxyIpDataRepository.findByIpAndPort(proxyIpBO.getIp(), proxyIpBO.getPort());
			if (ComUtil.isEmpty(proxyIpData)){
				proxyIpData = new ProxyIpData();
				BeanUtils.copyProperties(proxyIpBO, proxyIpData);
				proxyIpData.setId(null);
				proxyIpDataRepository.save(proxyIpData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean update(ProxyIp proxyIp) {
		proxyIp.setUpdateTime(new Date());
		proxyIpRepository.save(proxyIp);
		return true;
	}

	@Override
	public boolean update(ProxyIpBO proxyIpBO) {
		log.debug("检查有效性");
		if (!checkIPUtils.checkValidIP(proxyIpBO.getIp(), proxyIpBO.getPort())) {
			deleteByPrimaryKey(proxyIpBO.getId()); // 不能使用则删除
		}
		ProxyIp px = new ProxyIp();
		BeanUtils.copyProperties(proxyIpBO, px);
		return update(px);
	}

	@Override
	public ProxyIp selectByIPPort(String ip, Integer port) {
		return proxyIpRepository.findByIpAndPort(ip, port);
	}


	@Override
	public ProxyIp selectRandomIP() {
		List<ProxyIp> list = selectCanUseALL();
		if (!ComUtil.isEmpty(list)){
			return list.get(new Random().nextInt(list.size()));
		}
		return null;
	}

	@Override
	public ProxyIp selectCheckRandomIP() {
		List<ProxyIp> list = selectCanUseALL();
		for (ProxyIp proxyIp : list){
			if (checkIPUtils.checkValidIP(proxyIp.getIp(), proxyIp.getPort())) {
				proxyIp.setUpdateTime(new Date());
				try { proxyIpRepository.save(proxyIp);}catch (Exception e){}
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
		log.info(list.size()+"---------废弃重新利用----结束---"+ DateUtil.formatAsDatetime(new Date()));
	}




	@Override
	public void addProxyAll(List<ProxyIp> list) {
		List<ProxyIp> proxyIpList = new ArrayList<>();
		for (ProxyIp proxyIp : list) {
			if (!map.containsKey(proxyIp.getIp()+proxyIp.getPort())){
				proxyIpList.add(proxyIp);
			}
		}
		proxyIpRepository.saveAll(proxyIpList);
	}

	private void reMap(){
		log.info("--------map:达到了20万,开始减半--------");
		map.forEach((k,v) -> {
			if (map.size()<100000){
				log.info("--------map:减半至10万-------");
				return;
			}
			map.remove(k);
		});
	}
	@Override
	public void addProxy(ProxyIpBO proxyIpBO) {
		try {
			//验证库有点大,处理一下
			if (map.size()>200000){
				reMap();
			}

			// 根据该IP是待入库的新IP或者数据库中的旧IP分两种情况判断
			if (proxyIpBO.getCheckType() == ProxyIpBO.CheckIPType.ADD) {
				insert(proxyIpBO);
				return;
			}
			if (proxyIpBO.getCheckType() == ProxyIpBO.CheckIPType.UPDATE) {
				update(proxyIpBO);
				return;
			}
		} catch (Exception e) {
			log.debug( e.toString());
		}
	}
}
