package cn.zifangsky.spider.gp;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.model.XueqiuGupiao;
import cn.zifangsky.model.XueqiuGupiaoGainian;
import cn.zifangsky.mq.producer.GupiaoSender;
import cn.zifangsky.repository.XueqiuGupiaoGainianRepository;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
@Slf4j
@Component("xinlangGainianPipeline")
public class XinlangGainianPipeline implements Pipeline {


	@Resource(name="xueqiuGupiaoGainianRepository")
	private XueqiuGupiaoGainianRepository xueqiuGupiaoGainianRepository;
	
	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<String> result = resultItems.get("result");
		String symbol = resultItems.get("symbol");

		for (int i = 0; i < result.size(); i++) {
			XueqiuGupiaoGainian xueqiuGupiaoGainian = xueqiuGupiaoGainianRepository.findBySymbolAndGainian(symbol,result.get(i));
			if (ComUtil.isEmpty(xueqiuGupiaoGainian)){
				xueqiuGupiaoGainian = new XueqiuGupiaoGainian();
				xueqiuGupiaoGainian.setGainian(result.get(i));
				xueqiuGupiaoGainian.setSymbol(symbol);
				xueqiuGupiaoGainianRepository.save(xueqiuGupiaoGainian);
			}
		}
	}


}
