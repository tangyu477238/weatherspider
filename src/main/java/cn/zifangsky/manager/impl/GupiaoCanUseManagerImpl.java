package cn.zifangsky.manager.impl;

import cn.zifangsky.login.LoginManager;
import cn.zifangsky.manager.GupiaoCanUseManager;
import cn.zifangsky.model.GupiaoCanUse;
import cn.zifangsky.repository.GupiaoCanUseRepository;
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

	@Resource
	private LoginManager loginManager;


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

	@Override
	public void listBuy() {
		try {
			Map ymdMap = loginManager.listMyYmd(); //获取条件列表

			List<Map<String,Object>> list = gupiaoCanUseRepository.listBuy();
			for (Map<String,Object> map : list){
				String stock_code = map.get("symbol").toString();
				log.info(stock_code);
				int currentNum = loginManager.getCurrentAmount(stock_code); //当前数量
				int buyNum = 10; //参考数量
				Double lossPrice = Double.valueOf(map.get("lossPrice").toString()); //止损
				double newPrice = 0;
				if(currentNum>=buyNum){
					if (ymdMap.containsKey(stock_code+"34")){
						continue;
					}
					newPrice = Double.parseDouble(loginManager.getNewPrice(stock_code)); //获取最新价格
					loginManager.hungSell(stock_code,stock_code,""+lossPrice, ""+newPrice, buyNum);
					continue;
				}
				if (lossPrice==0){
					newPrice = Double.parseDouble(loginManager.getNewPrice(stock_code)); //获取最新价格
				}
				if (newPrice>lossPrice){
					String original_price = String.valueOf(newPrice+0.01); //获取触发价格
//					loginManager.hungBuy(stock_code, stock_code ,original_price , ""+newPrice, buyNum);
//					loginManager.hungSell(stock_code,stock_code,""+lossPrice, ""+newPrice, buyNum);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
