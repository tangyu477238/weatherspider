package cn.zifangsky.manager;

import cn.zifangsky.model.XueqiuGupiao;
import cn.zifangsky.model.XueqiuGupiaoKline;

public interface DongfangManager {

	void listGupiaoData();
//	void getDataXueqiuDetail(String bondId);

	void getKline(String bondId, String period, long Timestamp);

	void saveXueqiuKline(XueqiuGupiaoKline xueqiuGupiaoKline);

	void saveGupiao(XueqiuGupiao xueqiuGupiao);


}
