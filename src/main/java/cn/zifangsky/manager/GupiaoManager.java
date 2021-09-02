package cn.zifangsky.manager;

import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.model.Gupiao;

import java.util.List;

public interface GupiaoManager {
	void updateTime(String bondId);

	void saveKlineAll(List<BaseGupiaoKline> list);

	BaseGupiaoKline getGupiaoKline(String bondId, String bizDate, Integer period);


	List<Gupiao> listKzz();


	boolean getKlineMaxBizdate(String bondId, Integer period);


	void sysnKzzKlineAll(Integer period);

	List<Gupiao> listBeforeTime(Integer period);

	Gupiao listBeforeTime(String symbol, Integer period);
}
