package cn.zifangsky.manager.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.zifangsky.common.ComUtil;
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
				if (currentNum==0){
					continue;
				}
				int buyNum = 10; //参考数量
				BigDecimal lossPrice = getPrice(new BigDecimal(stockMap.get("lossPrice").toString()),new BigDecimal(-0.2)); //止损
				log.info(stock_code+"-lossPrice--"+lossPrice);
				BigDecimal newPrice = new BigDecimal(0);
				if(currentNum>0){ //已有数量/但数量不相等，重新挂一遍
					if (loginManager.checkAddYmd(ymdMap,stock_code,currentNum,"34", lossPrice)){  //是否有hungSell订单
						continue;
					}
					newPrice = new BigDecimal(loginManager.getNewPrice(stock_code)); //获取最新价格
					loginManager.hungSell(stock_code,stockMap.get("name").toString(),lossPrice.toString(), ""+newPrice, currentNum);
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
					loginManager.hungBuy(stock_code, stockMap.get("name").toString() ,original_price , newPrice.toString(), buyNum);
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

	private BigDecimal getPrice(BigDecimal price, BigDecimal percent){
		BigDecimal a = price.multiply(percent);
		BigDecimal b = a.divide(new BigDecimal(100) ,3, BigDecimal.ROUND_HALF_UP);
		return price.add(b);
	}



//	private void sellYmd(Map ymdMap, String stock_code) throws Exception{
//		if (ymdMap.containsKey(stock_code+"34")){
//			return;
//		}
//		Double newPrice = Double.parseDouble(loginManager.getNewPrice(stock_code)); //获取最新价格
//		loginManager.hungSell(stock_code,stock_code,""+ lossPrice, ""+newPrice, currentNum);
//		continue;
//	}


	/****
	 * 检查非需要处理的数据，删除掉
	 * @param listBuy
	 * @throws Exception
	 */
	private void listSell(List<Map<String,Object>> listBuy) throws Exception{
		Integer num = 10;
		//获取条件列表
		Map ymdMap = loginManager.listMyYmd();
		//仓数据列表
		List<JSONObject> list = loginManager.queryMyStockAmount();
		for (JSONObject jsonObject : list){
			try {
				Thread.sleep(20);
				Integer enable_amount = jsonObject.getInt("enable_amount");
				String stock_code = jsonObject.getStr("stock_code");
				String stock_name = jsonObject.getStr("stock_name");
				if (checkTodayData(listBuy, stock_code)){
					if (enable_amount > num){
						loginManager.hungSellByStoreCode(stock_code, stock_name, (enable_amount - num));
					}
					continue;
				}
				if (stock_code.startsWith("11")||stock_code.startsWith("12")){
					//已存在hungSell订单
					loginManager.delYmd(ymdMap, stock_code,"34");
					loginManager.hungSellByStoreCode(stock_code, stock_name, enable_amount);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void listMa() throws Exception{
//		清除所有
		loginManager.deleteAllMyYmd(null);
		//待处理清单
		List<Map<String,Object>> listBuyMa = gupiaoCanUseRepository.listBuyMa();
		//先处理掉数据
		listSell(listBuyMa);
		if(ComUtil.isEmpty(listBuyMa)){
			return;
		}
		//获取条件列表
		Map ymdMap = loginManager.listMyYmd();
		for (Map<String,Object> stockMap : listBuyMa) {
			try {
				Thread.sleep(50);
				String stock_code = stockMap.get("symbol").toString();
				String stock_name = stockMap.get("name").toString();
				//已存在hungBuy订单
				loginManager.delYmd(ymdMap,stock_code,"8");
				if (loginManager.getCurrentAmount(stock_code) > 0){
					continue;
				}
				loginManager.hungBuyByStoreCode(stock_code, stock_name,10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sellMorn() throws Exception{
		//仓数据列表
		List<JSONObject> list = loginManager.queryMyStockAmount();
		for (JSONObject jsonObject : list){
			try {
				Thread.sleep(50);
				Integer enable_amount = jsonObject.getInt("enable_amount");
				String stock_code = jsonObject.getStr("stock_code");
				String stock_name = jsonObject.getStr("stock_name");
				if (stock_code.startsWith("11")||stock_code.startsWith("12")){
					loginManager.hungSellByStoreCode(stock_code, stock_name, enable_amount);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *  检查是否是今天更新名单
	 * @param listBuy
	 * @param stock_code
	 * @return
	 */
	private boolean checkTodayData(List<Map<String,Object>> listBuy, String stock_code){
		for (Map<String,Object> stockMap : listBuy) {
			if (stock_code.equals(stockMap.get("symbol").toString())){
				return true;
			}
		}
		return false;
	}
}
