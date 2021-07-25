package cn.zifangsky.mq.consumer;

import cn.zifangsky.manager.XueqiuManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Component("gupiaoCodeKlineReceiver")
@Slf4j
public class GupiaoCodeKlineReceiver {

	@Value("${mq.kline.off}")
	private String consumerOff;

	@Resource
	private XueqiuManager xueqiuManager;



	/**
	 * 接收消息并处理
	 */


	@KafkaListener(id = "gpcodeklineid0", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "0" }) })
	public void handle0(String code) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，000000000 分区 gupiaoCodeKlineReceiver:{0}", code));
		getKlineData(code);
	}
	@KafkaListener(id = "gpcodeklineid1", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "1" }) })
	public void handle1(String code) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，11111111 分区  gupiaoCodeKlineReceiver:{0}", code));
		getKlineData(code);
	}
	@KafkaListener(id = "gpcodeklineid2", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "2" }) })
	public void handle2(String code) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，22222222 分区  gupiaoCodeKlineReceiver:{0}", code));
		getKlineData(code);
	}
	@KafkaListener(id = "gpcodeklineid3", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "3" }) })
	public void handle3(String code) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，33333333 分区  gupiaoCodeKlineReceiver:{0}", code));
		getKlineData(code);
	}
	@KafkaListener(id = "gpcodeklineid4", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "4" }) })
	public void handle4(String code) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，44444444 分区  gupiaoCodeKlineReceiver:{0}", code));
		getKlineData(code);
	}
	@KafkaListener(id = "gpcodeklineid5", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "5" }) })
	public void handle5(String code) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，55555555 分区  gupiaoCodeKlineReceiver:{0}", code));
		getKlineData(code);
	}



	private void getKlineData(String code){
		try {
			xueqiuManager.getDataXueqiuDetailKline(code.toUpperCase(), "day",System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
