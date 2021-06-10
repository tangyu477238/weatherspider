package cn.zifangsky.spider.gp;

import cn.zifangsky.model.XueqiuGupiao;
import cn.zifangsky.mq.producer.GupiaoSender;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;

/**
 * 自定义Pipeline处理抓取的数据
 * @author zifangsky
 *
 */
@Component("xueqiuGupiaoPipeline")
public class XueqiuGupiaoPipeline implements Pipeline {


	@Resource(name="gupiaoSender")
	private GupiaoSender gupiaoSender;
	
	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {

		String result = resultItems.get("result");
		JSONArray jsonArray = JSONObject.parseObject(result).getJSONObject("data").getJSONArray("list");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			XueqiuGupiao gupiao = JSONObject.toJavaObject(jsonObject, XueqiuGupiao.class);
			gupiaoSender.send(gupiao);

		}

	}


}
