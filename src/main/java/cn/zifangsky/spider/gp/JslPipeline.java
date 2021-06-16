package cn.zifangsky.spider.gp;

import cn.hutool.core.date.DateTime;
import cn.zifangsky.model.JslKzz;
import cn.zifangsky.mq.producer.GupiaoCodeKlineSender;
import cn.zifangsky.mq.producer.GupiaoGainianSender;
import cn.zifangsky.repository.JslKzzRepository;
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
@Component("jslPipeline")
public class JslPipeline implements Pipeline {
	@Resource
	private JslKzzRepository jslKzzRepository;

	@Resource
	private GupiaoCodeKlineSender gupiaoCodeKlineSender; //获取k线列表

	@Resource
	private GupiaoGainianSender gupiaoGainianSender;


	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {

		String result = resultItems.get("result");
		JSONArray jsonArray = JSONObject.parseObject(result).getJSONArray("rows");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String key =  jsonObject.getString("id");
			JSONObject jslkzz = jsonObject.getJSONObject("cell");
			JslKzz kzz = JSONObject.toJavaObject(jslkzz, JslKzz.class);
			kzz.setSscDt(DateTime.now().toDateStr());
			JslKzz kzz1 = jslKzzRepository.findByBondId(kzz.getBondId());
			if (kzz1 != null){
				kzz.setId(kzz1.getId());
			}
			jslKzzRepository.save(kzz);

			//获取1年的k线
			gupiaoCodeKlineSender.send(kzz.getPreBondId().toUpperCase());

			//开始取k线数据
//			gupiaoCodeKlineSender.send(kzz.getPreBondId().toUpperCase());

//			//开始生成对应的所属概念
//			gupiaoGainianSender.send(kzz.getStockCd());

		}

	}


}
