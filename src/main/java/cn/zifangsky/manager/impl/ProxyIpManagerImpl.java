package cn.zifangsky.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.zifangsky.repository.ProxyIpRepository;
import cn.zifangsky.spider.CheckIPUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.ProxyIp;

import javax.annotation.Resource;

@Service("proxyIpManager")
public class ProxyIpManagerImpl implements ProxyIpManager {
//	@Autowired
//	private ProxyIpMapper proxyIpMapper;


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

//	@Override
//	public int insertSelective(ProxyIp proxyIp) {
////		proxyIpMapper.insertSelective(proxyIp);
//		return 1;
//	}

	@Override
	public ProxyIp selectByPrimaryKey(Long id) {
		return proxyIpRepository.findById(id).orElse(null);
	}

//	@Override
//	public int updateByPrimaryKeySelective(ProxyIp proxyIp) {
//
//		return proxyIpMapper.updateByPrimaryKeySelective(proxyIp);
//	}

	@Override
	public int updateByPrimaryKey(ProxyIp proxyIp) {
		return insert(proxyIp);
	}

//	@Override
//	public Long findAllCount(ProxyIp proxyIp) {
////		proxyIpMapper.findAllCount(proxyIp);
//		return 0l;
//	}
//
//	@Override
//	public List<ProxyIp> findAll(PageInfo pageInfo, ProxyIp proxyIp) {
//		// 分页查询
//		// PageRequest.of 的第一个参数表示第几页（注意：第一页从序号0开始），第二个参数表示每页的大小
////		Pageable pageable = PageRequest.of(pageInfo.getFrom(), pageInfo.get); //查第二页
////		return proxyIpMapper.findAll(pageInfo, proxyIp);
//		proxyIpRepository.findAll();
//		return null;
//	}

	@Override
	public ProxyIp selectByIPPort(String ip, Integer port) {
		return proxyIpRepository.findByIpAndPort(ip, port);
	}

	@Override
	public ProxyIp selectRandomIP() {
		List<ProxyIp> list = selectAll();
		if(list == null || list.isEmpty()){
			return null;
		}
		int size = list.size();
		Random random = new Random();
		ProxyIp proxyIp = list.get(random.nextInt(size));
		if (CheckIPUtils.checkValidIP(proxyIp.getIp(), proxyIp.getPort())) {
			return proxyIp;
		}
		try {deleteByPrimaryKey(proxyIp.getId());}catch (Exception e){}

		return selectRandomIP();
	}

}
