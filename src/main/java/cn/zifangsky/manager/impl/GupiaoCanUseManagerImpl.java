package cn.zifangsky.manager.impl;

import cn.zifangsky.login.LoginManager;
import cn.zifangsky.manager.GupiaoCanUseManager;
import cn.zifangsky.model.GupiaoCanUse;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
			gupiaoCanUse.setCreateTime(new Date());
			canUseList.add(gupiaoCanUse);
		}
		addCanUse(canUseList);
	}

	@Override
	public void listBuy() {
		try {
			Map ymdMap = loginManager.listMyYmd(); //获取条件列表
			log.info(ymdMap.toString());
			List<Map<String,Object>> list = gupiaoCanUseRepository.listBuy();
			for (Map<String,Object> stockMap : list){
				Thread.sleep(500);
				String stock_code = stockMap.get("symbol").toString();
				int currentNum = loginManager.getCurrentAmount(stock_code); //当前数量
				int buyNum = 10; //参考数量
				BigDecimal lossPrice = new BigDecimal(stockMap.get("lossPrice").toString()); //止损
				log.info(stock_code+"-lossPrice--"+lossPrice);
				BigDecimal newPrice = new BigDecimal(0);
				if(currentNum>0){ //已有数量/但数量不相等，重新挂一遍
					if (loginManager.checkAddYmd(ymdMap,stock_code,currentNum,"34", lossPrice)){  //是否有hungSell订单
						continue;
					}
					newPrice = new BigDecimal(loginManager.getNewPrice(stock_code)); //获取最新价格
					loginManager.hungSell(stock_code,stock_code,lossPrice.toString(), ""+newPrice, currentNum);
					continue;
				}

//				--------------分界线---------------
				Thread.sleep(500);
				loginManager.delYmd(ymdMap,stock_code,"8"); //已存在hungBuy订单
				if (newPrice.intValue()==0){
					newPrice = new BigDecimal(loginManager.getNewPrice(stock_code)); //获取最新价格
				}
				log.info(stock_code+"-newPrice--"+newPrice);
				if (getFudu(newPrice, lossPrice).compareTo(new BigDecimal(0.3)) > 0
						&& (getFudu(newPrice, lossPrice).compareTo(new BigDecimal(2))<0) ){
					String original_price = String.valueOf(newPrice.add(new BigDecimal(0.01))); //获取触发价格
					loginManager.hungBuy(stock_code, stock_code ,original_price , newPrice.toString(), buyNum);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BigDecimal getFudu(BigDecimal newPrice, BigDecimal lossPrice){
		BigDecimal a = newPrice.subtract(lossPrice);
		BigDecimal b = a.divide(lossPrice ,3, BigDecimal.ROUND_HALF_UP);
		BigDecimal f = b.multiply(new BigDecimal(100));
		return f;
	}

//	private void sellYmd(Map ymdMap, String stock_code) throws Exception{
//		if (ymdMap.containsKey(stock_code+"34")){
//			return;
//		}
//		Double newPrice = Double.parseDouble(loginManager.getNewPrice(stock_code)); //获取最新价格
//		loginManager.hungSell(stock_code,stock_code,""+ lossPrice, ""+newPrice, currentNum);
//		continue;
//	}
}
