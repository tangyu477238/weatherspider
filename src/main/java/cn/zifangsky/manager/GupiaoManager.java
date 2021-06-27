package cn.zifangsky.manager;

import cn.zifangsky.model.GupiaoKline;

public interface GupiaoManager {


	void saveKline(GupiaoKline gupiaoKline);

	GupiaoKline getGupiaoKline(String bondId, String period, String bizDate);

}
