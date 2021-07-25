package cn.zifangsky.mq.consumer;

import cn.zifangsky.manager.XinlangManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Component("gupiaoGainianReceiver")
@Slf4j
public class GupiaoGainianReceiver {

	@Value("${mq.kline.off}")
	private String consumerOff;

	@Resource
	private XinlangManager xinlangManager;

	/**
	 * 接收消息并处理
	 */


	@KafkaListener(id = "gupiaoGainian0", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoGainian}"), partitions = { "0" }) })
	public void handle0(String gupiao) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，000000000 分区 gupiao:{0}", gupiao));
		saveGupiaoGainian(gupiao);
	}

	@KafkaListener(id = "gupiaoGainian1", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoGainian}"), partitions = { "1" }) })
	public void handle1(String gupiao) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，11111111 分区 gupiao:{0}", gupiao));
		saveGupiaoGainian(gupiao);
	}
	@KafkaListener(id = "gupiaoGainian2", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoGainian}"), partitions = { "2" }) })
	public void handle2(String gupiao) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，22222222 分区 gupiao:{0}", gupiao));
		saveGupiaoGainian(gupiao);
	}
	@KafkaListener(id = "gupiaoGainian3", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoGainian}"), partitions = { "3" }) })
	public void handle3(String gupiao) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，33333333 分区 gupiao:{0}", gupiao));
		saveGupiaoGainian(gupiao);
	}
	@KafkaListener(id = "gupiaoGainian4", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoGainian}"), partitions = { "4" }) })
	public void handle4(String gupiao) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，44444444 分区 gupiao:{0}", gupiao));
		saveGupiaoGainian(gupiao);
	}
	@KafkaListener(id = "gupiaoGainian5", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoGainian}"), partitions = { "5" }) })
	public void handle5(String gupiao) {
		if ("0".equals(consumerOff)) return;
		log.info(MessageFormat.format("接收到消息，55555555 分区 gupiao:{0}", gupiao));
		saveGupiaoGainian(gupiao);
	}


	private void saveGupiaoGainian(String gupiao){
		xinlangManager.getGupiaoGainian(gupiao);
	}
}
