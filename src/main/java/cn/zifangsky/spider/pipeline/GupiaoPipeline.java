package cn.zifangsky.spider.pipeline;

import cn.zifangsky.model.Gupiao;
import cn.zifangsky.repository.GupiaoRepository;
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

	@Resource
	private GupiaoRepository gupiaoRepository;


	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<Gupiao> list = resultItems.get("result");
		gupiaoRepository.saveAll(list);
		//清除不符合的数据
//		gupiaoRepository.delNotUse();

	}


}
