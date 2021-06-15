package cn.zifangsky.manager;

import cn.zifangsky.model.XueqiuGupiao;
import cn.zifangsky.model.GupiaoKline;

public interface XueqiuManager {

	void listGupiaoData();
//	void getDataXueqiuDetail(String bondId);

	void getDataXueqiuDetailKline(String bondId, String period, long Timestamp);

	void saveXueqiuKline(GupiaoKline gupiaoKline);

	void saveGupiao(XueqiuGupiao xueqiuGupiao);


}
