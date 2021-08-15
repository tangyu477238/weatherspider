package cn.zifangsky.manager;

public interface DongfangManager {

	//更新所有股票信息
	void listGupiaoData();

	//获取K线信息
	void getKline(String bondId, String period, boolean isProxy, boolean isToday);
	void getKline(String bondId, String period, boolean isProxy);
	void getKline(String bondId, String period);

	void getKlineDay(String bondId);
	void getKline5M(String bondId);

	void getToDayKline(String bondId);
	void getToDayKline5M(String bondId);

	void listKzzData();




}
