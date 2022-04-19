package cn.zifangsky.manager.impl;

import cn.hutool.json.JSONObject;
import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.common.ExecutorProcessPool;
import cn.zifangsky.login.LoginManager;
import cn.zifangsky.manager.LastMornManager;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import cn.zifangsky.repository.GupiaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("lastMornManagerImpl")
public class LastMornManagerImpl implements LastMornManager {

	@Value("${cczq.hs_openid}")
	private String hs_openid ;

	@Resource
	private GupiaoCanUseRepository gupiaoCanUseRepository;

	@Resource
	private LoginManager loginManager;

	@Resource
	private GupiaoRepository gupiaoRepository;



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

	/**
	 * 立刻清理所有
	 */
	private void listSellAll() throws Exception {
		//仓库列表
		List<JSONObject> stockList = loginManager.queryMyStockAmount();
		//获取条件列表
		Map ymdMap = loginManager.listMyYmd();
		//sell数据
		listSell(new ArrayList<>(), stockList, ymdMap);
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


	/**
	 * 立刻购入所有
	 */
	private void listBuyAll(List<Map<String,Object>> listBuy) throws Exception {
		Collections.shuffle(listBuy);
		//仓库列表
		List<JSONObject> stockList = loginManager.queryMyStockAmount();
		//获取条件列表
		Map ymdMap = loginManager.listMyYmd();
		//先处理多买的数据
		listSell(listBuy, stockList, ymdMap);
		//重新buy不足的数据
		listBuy(listBuy, stockList, ymdMap);
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

	private boolean in(List<Double> list){
		Double first = 0.0;
		Double two = 0.0;
		Double now = 0.0;
		boolean flag = false;
		for (int i = 2 ; list.size() > 2 && i < list.size(); i++){
			first = list.get(i-2);
			two = list.get(i-1);
			now = list.get(i);
			if (first < two && two < now && flag){

			}
		}
		return false;
	}



	@Override
	public void listGrid() throws Exception {
		for (int x = 1 ;x < 3; x++){

		String bBizDate = gupiaoRepository.getBizDate(x);
		log.info(bBizDate);
		List<Map<String, Object>> list = gupiaoCanUseRepository.listGrid(bBizDate);
		Double first = 0.0;
		Double two = 0.0;
		Double three = 0.0;
		Double now = 0.0;

		Double beg = 0.0;
		Double end = 0.0;
		boolean flag = false;
		Double sum = 0.0;
		for (int i = 3 ; list.size() > 3 && i < list.size(); i++){
			first = Double.parseDouble(list.get(i-3).get("bilv").toString());
			two = Double.parseDouble(list.get(i-2).get("bilv").toString());
			three = Double.parseDouble(list.get(i-1).get("bilv").toString());
			now = Double.parseDouble(list.get(i).get("bilv").toString());
			String dividend_yield = list.get(i).get("dividend_yield").toString();
			//2<3<当前且已出
			if (first < two && two < three && three < now && !flag && now < 2 && now>0){
				beg  = now;
				flag = true;
				log.info(now + "-------进-------"+dividend_yield);
				//1>2>3>当前且已进
			} else if (first > two && two > three && three > now && flag){
				end = now;
				flag  = false;
				log.info(now + "-----出--------///"+(end-beg)+"///"+dividend_yield);
				sum = sum +(end-beg);
			}
		}
		log.info("sum-----///"+sum);

		}

//		//清除所有
//		loginManager.deleteAllMyYmd(null);
//		if (flag){
//			//待处理清单
//			List<Map<String,Object>> listBuyGrid = gupiaoCanUseRepository.listBuyGrid(hs_openid);
//			listBuyAll(listBuyGrid);
//		} else {
//			//清理所有
//			listSellAll();
//		}



	}
}
