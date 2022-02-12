package cn.zifangsky.spider.pipeline;

import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.model.GupiaoEvery;
import cn.zifangsky.repository.GupiaoEveryRepository;
import cn.zifangsky.repository.GupiaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义Pipeline处理抓取的数据
 * @author zifangsky
 *
 */
@Service
public class GupiaoEveryPipeline implements Pipeline {

	@Resource
	private GupiaoEveryRepository gupiaoEveryRepository;

	@Resource
	private GupiaoRepository gupiaoRepository;


	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<Gupiao> list = resultItems.get("result");
		String createTime = DateTimeUtil.formatDateStr(new Date(), DateTimeUtil.FMT_yyyyMMddHHmm);
		String bBizDate = gupiaoRepository.getBizDate(1);
		List<GupiaoEvery> everyList = list.stream().map(x -> {
			GupiaoEvery gupiaoEveryPipeline = new GupiaoEvery();
			BeanUtils.copyProperties(x, gupiaoEveryPipeline);
			gupiaoEveryPipeline.setDividend_yield(createTime);
			gupiaoEveryPipeline.setVolume_ratio(bBizDate);
			return gupiaoEveryPipeline;
		}).collect(Collectors.toList());

		gupiaoEveryRepository.saveAll(everyList);

	}


}
