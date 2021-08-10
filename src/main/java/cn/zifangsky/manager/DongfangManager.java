package cn.zifangsky.manager;

public interface DongfangManager {

	//更新所有股票信息
	void listGupiaoData();

	//获取K线信息
	void getKline(String bondId, String period, boolean flag);

	void listKzzData();




}
