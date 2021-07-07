package cn.zifangsky.mq.consumer;

import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.model.GupiaoKline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Component("gupiaoKlineReceiver")
@Slf4j
public class GupiaoKlineReceiver {

	@Resource
	private GupiaoManager gupiaoManager;

	@Value("${mq.kline.off}")
	private String consumerOff;

	/**
	 * 接收消息并处理
	 */

	@KafkaListener(id = "gpklineid0", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "0" }) })
	public void handle0(GupiaoKline gupiaoKline) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，000000000 分区 代理IP:{0}", gupiaoKline));
		saveGupiaoKline(gupiaoKline);
	}

	@KafkaListener(id = "gpklineid1", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "1" }) })
	public void handle1(GupiaoKline gupiaoKline) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，11111111 分区 代理IP:{0}", gupiaoKline));
		saveGupiaoKline(gupiaoKline);
	}
	@KafkaListener(id = "gpklineid2", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "2" }) })
	public void handle2(GupiaoKline gupiaoKline) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，22222222 分区 代理IP:{0}", gupiaoKline));
		saveGupiaoKline(gupiaoKline);
	}
	@KafkaListener(id = "gpklineid3", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "3" }) })
	public void handle3(GupiaoKline gupiaoKline) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，33333333 分区 代理IP:{0}", gupiaoKline));
		saveGupiaoKline(gupiaoKline);
	}
	@KafkaListener(id = "gpklineid4", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "4" }) })
	public void handle4(GupiaoKline gupiaoKline) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，44444444 分区 代理IP:{0}", gupiaoKline));
		saveGupiaoKline(gupiaoKline);
	}
	@KafkaListener(id = "gpklineid5", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoKline}"), partitions = { "5" }) })
	public void handle5(GupiaoKline gupiaoKline) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，55555555 分区 代理IP:{0}", gupiaoKline));
		saveGupiaoKline(gupiaoKline);
	}


	private void saveGupiaoKline(GupiaoKline gupiaoKline){
		gupiaoManager.saveKline(gupiaoKline);
	}
}
