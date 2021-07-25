package cn.zifangsky.spider.gp;

import cn.zifangsky.model.BizOrder;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.mq.producer.GupiaoSender;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义Pipeline处理抓取的数据
 * @author zifangsky
 *
 */
@Service
public class GupiaoPipeline implements Pipeline {


	@Resource(name="gupiaoSender")
	private GupiaoSender gupiaoSender;
	
	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<Gupiao> list = resultItems.get("result");
		for (Gupiao gupiao : list){
			gupiaoSender.send(gupiao);
		}

	}


}
