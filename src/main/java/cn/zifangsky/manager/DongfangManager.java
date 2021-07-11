package cn.zifangsky.manager;

import cn.zifangsky.model.XueqiuGupiao;
import cn.zifangsky.model.GupiaoKline;

public interface DongfangManager {

	void listGupiaoData();
//	void getDataXueqiuDetail(String bondId);

	void getKline(String bondId, String period, boolean flag);

	void saveXueqiuKline(GupiaoKline gupiaoKline);

	void saveGupiao(XueqiuGupiao xueqiuGupiao);


}
