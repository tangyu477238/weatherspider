package cn.zifangsky.spider;

import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.ProxyIp;
import cn.zifangsky.model.bo.ProxyIpBO;
import cn.zifangsky.model.bo.ProxyIpBO.CheckIPType;
import cn.zifangsky.mq.producer.CheckIPSender;
import org.springframework.beans.factory.annotation.Value;
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
@Component("proxyIPPipeline")
public class ProxyIPPipeline implements Pipeline {

	@Value("${mq.topicName.checkIP}")
	private String checkIPTopicName;
	
	@Resource(name="checkIPSender")
	private CheckIPSender checkIPSender;



	@Resource
	private ProxyIpManager proxyIpManager;
	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<ProxyIp> list = resultItems.get("result");
		
		if(list != null && list.size() > 0){
//			list.forEach(proxyIp -> {
//				ProxyIpBO proxyIpBO = new ProxyIpBO();
//				proxyIpBO.setId(proxyIp.getId());
//				proxyIpBO.setIp(proxyIp.getIp());
//				proxyIpBO.setPort(proxyIp.getPort());
//				proxyIpBO.setType(proxyIp.getType());
//				proxyIpBO.setAddr(proxyIp.getAddr());
//				proxyIpBO.setUsed(0);
//				proxyIpBO.setOther(proxyIp.getOther());
//				proxyIpBO.setCheckType(CheckIPType.ADD);
//
//				//检测任务添加到队列中
////				checkIPSender.send(checkIPTopicName, proxyIpBO);
//			});

			proxyIpManager.addProxyAll(list);
		}

	}

}
