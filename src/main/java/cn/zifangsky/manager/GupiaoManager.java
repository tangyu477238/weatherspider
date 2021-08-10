package cn.zifangsky.manager;

import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.model.Gupiao;

import java.util.List;

public interface GupiaoManager {


	void saveKline(BaseGupiaoKline gupiaoKline);

	BaseGupiaoKline getGupiaoKline(String bondId, String period, String bizDate);

	void saveGupiao(Gupiao gupiao);

	void updateAllGupiaoKline();

	List<Gupiao> listKzz();
}
