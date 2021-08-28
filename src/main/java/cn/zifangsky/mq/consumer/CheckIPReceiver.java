package cn.zifangsky.mq.consumer;

import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.bo.ProxyIpBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Component("checkIPReceiver")
@Slf4j
public class CheckIPReceiver {

	@Value("${mq.proxyIp.off}")
	private String consumerOff;

	@Resource(name = "proxyIpManager")
	private ProxyIpManager proxyIpManager;


//	@KafkaListener(topicPartitions = { @TopicPartition(topic = ("${mq.topicName.checkIP}"),
//			partitions = { "0","1","2","3","4","5","6","7","8","9",
//					"10","11","12","13","14","15","16","17","18","19",
//					"20","21","22","23","24","25","26","27","28","29",
//					"30","31","32","33","34","35","36","37","38","39",
//					"40","41","42","43","44"}) }, containerFactory = "batchContainerFactory")
//	@KafkaListener(topicPartitions = { @TopicPartition(topic = ("${mq.topicName.checkIP}"),
//			partitions = { "0","1","2","3","4","5","6","7","8","9",
//					"10","11","12","13","14","15","16","17","18","19",
//					"20","21","22","23","24","25"}) }, containerFactory = "batchContainerFactory")
	@KafkaListener(topicPartitions = { @TopicPartition(topic = ("${mq.topicName.checkIP}"),
			partitions = { "0","1","2","3","4","5","6","7","8","9",
					"10","11","12","13","14","15","16","17","18","19"}) }, containerFactory = "batchContainerFactory")
	public void handle0(ProxyIpBO proxyIpBO) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，代理IP:{0}", proxyIpBO));
		runCheckIp(proxyIpBO);
	}


	private void runCheckIp(ProxyIpBO proxyIpBO){
		try {
			proxyIpManager.addProxy(proxyIpBO);
		}catch (Exception e){log.debug(e.toString());}
	}

//	private void runCheckIp(ProxyIpBO proxyIpBO){
//		try {
//			Runnable run = new ProxyRunnable(proxyIpBO);
//			ExecutorProcessPool.getInstance().executeByCustomThread(run);
//		}catch (Exception e){log.debug(e.toString());}
//
//	}
//
//
//	public class ProxyRunnable implements Runnable{
//		private ProxyIpBO proxyIpBO;
//		public ProxyRunnable(ProxyIpBO proxyIpBO){
//			this.proxyIpBO=proxyIpBO;
//		}
//		@Override
//		public void run(){
//			proxyIpManager.addProxy(proxyIpBO);
//		}
//	}
}
