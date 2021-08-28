package cn.zifangsky.spider.pipeline;

import cn.zifangsky.model.BizOrder;
import cn.zifangsky.repository.BizOrderRepository;
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
@Component("bizOrderPipeline")
public class BizOrderPipeline implements Pipeline {
	@Resource
	private BizOrderRepository bizOrderRepository;


	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<BizOrder> list = resultItems.get("result");
		if(list != null && list.size() > 0){
			list.forEach(bizOrder -> {
				if (bizOrderRepository.findByOrderNo(bizOrder.getOrderNo())==null){
					bizOrderRepository.save(bizOrder);
				}
			});
		}
	}


}
