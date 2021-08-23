package cn.zifangsky.manager;

import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.model.Gupiao;

import java.util.List;

public interface GupiaoManager {

//	void saveKline(BaseGupiaoKline gupiaoKline);
	void saveKlineAll(List<BaseGupiaoKline> list);

	BaseGupiaoKline getGupiaoKline(String bondId, String bizDate, Integer period);

//	void saveGupiao(Gupiao gupiao);

//	void updateAllGupiaoKline();

	List<Gupiao> listKzz();


	boolean getKlineMaxBizdate(String bondId, Integer period);


	void sysnKzzKlineAll(Integer period);

}
