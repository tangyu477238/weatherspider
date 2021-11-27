package cn.zifangsky.manager.impl;

import cn.hutool.json.JSONObject;
import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.common.ExecutorProcessPool;
import cn.zifangsky.login.LoginManager;
import cn.zifangsky.manager.LastMornManager;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("lastMornManagerImpl")
public class LastMornManagerImpl implements LastMornManager {

	@Value("${cczq.hs_openid}")
	private String hs_openid ;

	@Resource
	private GupiaoCanUseRepository gupiaoCanUseRepository;

	@Resource
	private LoginManager loginManager;




	public class SellRunnable implements Runnable{
		private JSONObject jsonObject;
		private List<Map<String,Object>> listBuy;
		private Map ymdMap;

		public SellRunnable(JSONObject jsonObject, List<Map<String,Object>> listBuy, Map ymdMap){
			this.jsonObject = jsonObject;
			this.listBuy = listBuy;
			this.ymdMap = ymdMap;
		}
		@Override
		public void run(){
			try {

				Integer enable_amount = jsonObject.getInt("enable_amount");
				String stock_code = jsonObject.getStr("stock_code");
//				if ("110055".equals(stock_code)){
//					log.info(stock_code);
//				}
				String stock_name = jsonObject.getStr("stock_name");
				//取得今天的数量
				Integer useNum = getTodayData(listBuy, stock_code);
				if (useNum > 0){
					if (enable_amount > useNum){
						//已存在hungSell订单
						loginManager.delYmd(ymdMap, stock_code,"34");
						loginManager.hungSellByStoreCode(stock_code, stock_name, (enable_amount - useNum));
					}
					return;
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

	public class BuyRunnable implements Runnable{
		private Map<String,Object> buyMap;
		List<JSONObject> stockList;
		private Map ymdMap;

		public BuyRunnable(Map<String,Object> buyMap, List<JSONObject> stockList, Map ymdMap){
			this.buyMap = buyMap;
			this.stockList = stockList;
			this.ymdMap = ymdMap;
		}
		@Override
		public void run(){
			try {
				String stock_code = buyMap.get("symbol").toString();
				String stock_name = buyMap.get("name").toString();
				Double useNum = Double.parseDouble(buyMap.get("num").toString());
				//已有仓位
				int num = getEnableData(stockList, stock_code);
				if (num==-1){
					num = 0;
				}
				//大于或等于购买数
				if (num >= useNum){
					return;
				}
				//已存在hungBuy订单
				loginManager.delYmd(ymdMap, stock_code, "8");
				loginManager.hungBuyByStoreCode(stock_code, stock_name, (useNum.intValue()-num));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}


	/****
     * 检查非需要处理的数据，删除掉
	 * @param listBuy
	 * @param stockList
	 * @param ymdMap
	 */
	private void listSell(List<Map<String,Object>> listBuy, List<JSONObject> stockList, Map ymdMap) {
		if(ComUtil.isEmpty(stockList)){
			return;
		}
		for (JSONObject jsonObject : stockList){
			try {
				Thread.sleep(20);
				Runnable run = new SellRunnable(jsonObject, listBuy, ymdMap);
				ExecutorProcessPool.getInstance().executeByFixedThread(run);
			} catch (Exception e) {
				log.info(e.toString());
			}
		}
	}

	/****
	 * 检查非需要处理的数据，删除掉
	 * @param listBuy
	 * @param stockList
	 * @param ymdMap
	 */
	private void listBuy(List<Map<String,Object>> listBuy, List<JSONObject> stockList, Map ymdMap) {
		if(ComUtil.isEmpty(listBuy)){
			return;
		}
		for (Map<String,Object> buyMap : listBuy) {
			try {
				Thread.sleep(50);
				Runnable run = new BuyRunnable(buyMap, stockList, ymdMap);
				ExecutorProcessPool.getInstance().executeByFixedThread(run);
			} catch (Exception e) {
				log.debug(e.toString());
			}
		}
	}

	@Override
	public void listMa() throws Exception{

//		清除所有
		loginManager.deleteAllMyYmd(null);
		//待处理清单
		List<Map<String,Object>> listBuyMa = gupiaoCanUseRepository.listBuyLastMorn(hs_openid);
		Collections.shuffle(listBuyMa);
		//仓库列表
		List<JSONObject> stockList = loginManager.queryMyStockAmount();
		//获取条件列表
		Map ymdMap = loginManager.listMyYmd();

		//先处理sell数据
		listSell(listBuyMa, stockList, ymdMap);

		//重新buy数据
		listBuy(listBuyMa, stockList, ymdMap);

	}

//	@Override
//	public void sellMorn() throws Exception{
//		//仓数据列表
//		List<JSONObject> list = loginManager.queryMyStockAmount();
//		for (JSONObject jsonObject : list){
//			try {
//				Thread.sleep(50);
//				Integer enable_amount = jsonObject.getInt("enable_amount");
//				String stock_code = jsonObject.getStr("stock_code");
//				String stock_name = jsonObject.getStr("stock_name");
//				if (stock_code.startsWith("11")||stock_code.startsWith("12")){
//					loginManager.hungSellByStoreCode(stock_code, stock_name, enable_amount);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	/**
	 *  今天更新名单
	 * @param listBuy
	 * @param stock_code
	 * @return
	 */
	private Integer getTodayData(List<Map<String,Object>> listBuy, String stock_code){
		for (Map<String,Object> stockMap : listBuy) {
			if (stock_code.equals(stockMap.get("symbol").toString())){
				Double useNum = Double.parseDouble(stockMap.get("num").toString());
				return useNum.intValue();
			}
		}
		return 0;
	}

	/**
	 *  获取可用数量
	 * @param stockList
	 * @param stock_code
	 * @return
	 */
	private Integer getEnableData(List<JSONObject> stockList, String stock_code){
		for (JSONObject jsonObject : stockList) {
			if (stock_code.equals(jsonObject.getStr("stock_code"))){
				Integer enable_amount = jsonObject.getInt("enable_amount");
				return enable_amount;
			}
		}
		return 0;
	}
}
