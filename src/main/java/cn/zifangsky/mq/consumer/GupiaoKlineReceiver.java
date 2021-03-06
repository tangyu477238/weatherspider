package cn.zifangsky.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("gupiaoKlineReceiver")
@Slf4j
public class GupiaoKlineReceiver {

//	@Resource
//	private GupiaoManager gupiaoManager;
//
//	@Value("${mq.kline.off}")
//	private String consumerOff;
//
//	/**
//	 * 接收消息并处理
//	 */
//
//	@KafkaListener(id = "gpklineid0", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "0" }) })
//	public void handle0(BaseGupiaoKline gupiaoKline) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，000000000 分区 代理IP:{0}", gupiaoKline));
//		saveGupiaoKline(gupiaoKline);
//	}
//
//	@KafkaListener(id = "gpklineid1", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "1" }) })
//	public void handle1(BaseGupiaoKline gupiaoKline) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，11111111 分区 代理IP:{0}", gupiaoKline));
//		saveGupiaoKline(gupiaoKline);
//	}
//	@KafkaListener(id = "gpklineid2", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "2" }) })
//	public void handle2(BaseGupiaoKline gupiaoKline) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，22222222 分区 代理IP:{0}", gupiaoKline));
//		saveGupiaoKline(gupiaoKline);
//	}
//	@KafkaListener(id = "gpklineid3", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "3" }) })
//	public void handle3(BaseGupiaoKline gupiaoKline) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，33333333 分区 代理IP:{0}", gupiaoKline));
//		saveGupiaoKline(gupiaoKline);
//	}
//	@KafkaListener(id = "gpklineid4", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "4" }) })
//	public void handle4(BaseGupiaoKline gupiaoKline) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，44444444 分区 代理IP:{0}", gupiaoKline));
//		saveGupiaoKline(gupiaoKline);
//	}
//	@KafkaListener(id = "gpklineid5", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "5" }) })
//	public void handle5(BaseGupiaoKline gupiaoKline) {
//		if ("0".equals(consumerOff)) return;
//		log.info(MessageFormat.format("接收到消息，55555555 分区 代理IP:{0}", gupiaoKline));
//		saveGupiaoKline(gupiaoKline);
//	}
//
//
//	private void saveGupiaoKline(BaseGupiaoKline gupiaoKline){
//		try {
//			Thread.sleep(300);
//			Runnable run = new GupiaoKlineRunnable(gupiaoKline);
//			ExecutorProcessPool.getInstance().executeByCustomThread(run);
//		}catch (Exception e){log.debug(e.toString());}
//	}
//
//	public class GupiaoKlineRunnable implements Runnable{
//		private BaseGupiaoKline gupiaoKline;
//		public GupiaoKlineRunnable(BaseGupiaoKline gupiaoKline){
//			this.gupiaoKline=gupiaoKline;
//		}
//		@Override
//		public void run(){
//			gupiaoManager.saveKline(gupiaoKline);
//		}
//	}
}
