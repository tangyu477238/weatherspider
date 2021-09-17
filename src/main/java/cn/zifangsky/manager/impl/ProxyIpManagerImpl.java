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


@Slf4j
@Service("proxyIpManager")
public class ProxyIpManagerImpl implements ProxyIpManager {

	private Map<String, ProxyIp> map = new ConcurrentHashMap<>();

	@Resource
	private ProxyIpDataRepository proxyIpDataRepository;
	@Resource
	private ProxyIpRepository proxyIpRepository;

	@Resource
	private CheckIPUtils checkIPUtils;

	@Override
	public List<ProxyIp> selectAll() { //未验证ip
		Integer randNum = new Random().nextInt(10000);
		List<ProxyIp> list = proxyIpRepository.listProxy(randNum);
		if (ComUtil.isEmpty(list)){
			synProxy(); //废弃IP重存
		}
		return list;
	}

	@Override
	public List<ProxyIp> selectCanUseALL() {
		Integer randNum = new Random().nextInt(50);
		List<ProxyIp> list = proxyIpRepository.listCanUse(randNum);
		if (!ComUtil.isEmpty(list)){
			return list;
		}
		return selectAll();
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		proxyIpRepository.deleteById(id);
		return 1;
	}

	@Override
	public void deleteByProxy(ProxyIp proxyIp) {
		try {
			proxyIp.setUpdateTime(null);
			proxyIpRepository.save(proxyIp);
		} catch (Exception e) {
			log.info(e.toString());
		}
	}

	@Override
	public void insertCheckProxy(ProxyIp proxyIp) {
		// 0 查询该IP是否 验证过
		StringBuffer stringBuffer = new StringBuffer(proxyIp.getIp());
		stringBuffer.append(proxyIp.getPort());
		if (map.containsKey(stringBuffer.toString())){
			return ;
		}
		map.put(stringBuffer.toString(), proxyIp); //存进集合

		// 1 查询该IP 是否已存在
		ProxyIp oldIP = selectByIPPort(proxyIp.getIp(), proxyIp.getPort());
		if (!ComUtil.isEmpty(oldIP)) {
			return ;
		}
		if (!checkIPUtils.checkValidIP(proxyIp.getIp(), proxyIp.getPort())) {
			return ;
		}
		updateProxy(proxyIp);
		log.debug("---收录成功---"+proxyIp.getIp() +":"+ proxyIp.getPort());
		addProxyData(proxyIp); //记录ip
	}

	private void addProxyData(ProxyIp proxyIp){
		try {
			ProxyIpData proxyIpData = proxyIpDataRepository.findByIpAndPort(proxyIp.getIp(), proxyIp.getPort());
			if (ComUtil.isEmpty(proxyIpData)){
				proxyIpData = new ProxyIpData();
				BeanUtils.copyProperties(proxyIp, proxyIpData);
				proxyIpData.setId(null);
				proxyIpDataRepository.save(proxyIpData);
			}
		} catch (Exception e) {
			log.debug(e.toString());
		}
	}

	@Override
	public ProxyIp updateProxy(ProxyIp proxyIp) {
		try {
			proxyIp.setUpdateTime(new Date());
			return proxyIpRepository.save(proxyIp);
		} catch (Exception e) {
			log.info(e.toString());
		}
		return proxyIp;
	}

	@Override
	public ProxyIp updateCheckProxy(ProxyIp proxyIp) {
		log.debug("检查有效性");
		if (!checkIPUtils.checkValidIP(proxyIp.getIp(), proxyIp.getPort())) {
			deleteByProxy(proxyIp); // 不能使用则删除
			return null;
		}
		return updateProxy(proxyIp);
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
//		Collections.shuffle(list);
		for (ProxyIp proxyIp : list){
			if (!ComUtil.isEmpty(updateCheckProxy(proxyIp))) {
				return proxyIp;
			}
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
			ProxyIp proxyIp = new ProxyIp();
			BeanUtils.copyProperties(proxyIpBO, proxyIp);
			// 根据该IP是待入库的新IP或者数据库中的旧IP分两种情况判断
			if (proxyIpBO.getCheckType() == ProxyIpBO.CheckIPType.ADD) {
				insertCheckProxy(proxyIp);
				return;
			}
			if (proxyIpBO.getCheckType() == ProxyIpBO.CheckIPType.UPDATE) {
				updateCheckProxy(proxyIp);
				return;
			}
		} catch (Exception e) {
			log.debug( e.toString());
		}
	}
}
