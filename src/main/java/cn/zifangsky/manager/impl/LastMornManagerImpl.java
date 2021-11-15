package cn.zifangsky.manager.impl;

import cn.hutool.json.JSONObject;
import cn.zifangsky.common.ComUtil;
import cn.zifangsky.login.LoginManager;
import cn.zifangsky.manager.LastMornManager;
import cn.zifangsky.repository.GupiaoCanUseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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


	/****
     * 检查非需要处理的数据，删除掉
	 * @param listBuy
     * @throws Exception
	 */
	private void listSell(List<Map<String,Object>> listBuy) throws Exception{
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
				Integer useNum = getTodayData(listBuy, stock_code);
				if (useNum > 0){
					if (enable_amount > useNum){
						loginManager.hungSellByStoreCode(stock_code, stock_name, (enable_amount - useNum));
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
		List<Map<String,Object>> listBuyMa = gupiaoCanUseRepository.listBuyLastMorn(hs_openid);
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
				Double useNum = Double.parseDouble(stockMap.get("num").toString());
				//已存在hungBuy订单
				loginManager.delYmd(ymdMap,stock_code,"8");
				if (loginManager.getCurrentAmount(stock_code) > 0){
					continue;
				}
				loginManager.hungBuyByStoreCode(stock_code, stock_name, useNum.intValue());
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
}
