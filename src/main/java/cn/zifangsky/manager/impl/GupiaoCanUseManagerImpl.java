package cn.zifangsky.manager.impl;

import cn.zifangsky.manager.GupiaoCanUseManager;
import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.model.GupiaoCanUse;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import cn.zifangsky.spider.ProxyIPPipeline;
import cn.zifangsky.spider.gp.JslPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("gupiaoCanUseManager")
public class GupiaoCanUseManagerImpl implements GupiaoCanUseManager {

	
	@Resource
	private GupiaoCanUseRepository gupiaoCanUseRepository;



	@Override
	public void addCanUse(List<GupiaoCanUse> list) {
		gupiaoCanUseRepository.saveAll(list);
	}

	@Override
	public void addCanUse() {
		List<Map<String,Object>> list = gupiaoCanUseRepository.listCanUseView();
		GupiaoCanUse gupiaoCanUse;
		List<GupiaoCanUse> canUseList = new ArrayList<>();
		for (Map<String,Object> map : list){
			gupiaoCanUse = new GupiaoCanUse();
			gupiaoCanUse.setSymbol(map.get("symbol").toString());
			gupiaoCanUse.setBizDate(map.get("biz_date").toString());
			gupiaoCanUse.setPeriod(Integer.parseInt(map.get("period").toString()));
			gupiaoCanUse.setLossPrice(Double.parseDouble(map.get("loss_price").toString()));
			gupiaoCanUse.setStype(Integer.parseInt(map.get("stype").toString()));
			canUseList.add(gupiaoCanUse);
		}
		addCanUse(canUseList);
	}
}
