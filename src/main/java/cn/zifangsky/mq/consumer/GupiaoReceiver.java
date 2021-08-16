package cn.zifangsky.mq.consumer;

import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.manager.XueqiuManager;
import cn.zifangsky.model.Gupiao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Component("gupiaoReceiver")
@Slf4j
public class GupiaoReceiver {

	@Value("${mq.kline.off}")
	private String consumerOff;

	@Resource
	private GupiaoManager gupiaoManager;

	@Resource
	private DongfangManager dongfangManager;


	/**
	 * 接收消息并处理
	 */


//	@KafkaListener(id = "gupiao0", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiao}"), partitions = { "0" }) })
//	public void handle0(Gupiao gupiao) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，000000000 分区 gupiao:{0}", gupiao));
//		saveGupiao(gupiao);
//	}
//
//	@KafkaListener(id = "gupiao1", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiao}"), partitions = { "1" }) })
//	public void handle1(Gupiao gupiao) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，11111111 分区 gupiao:{0}", gupiao));
//		saveGupiao(gupiao);
//	}
//	@KafkaListener(id = "gupiao2", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiao}"), partitions = { "2" }) })
//	public void handle2(Gupiao gupiao) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，22222222 分区 gupiao:{0}", gupiao));
//		saveGupiao(gupiao);
//	}
//	@KafkaListener(id = "gupiao3", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiao}"), partitions = { "3" }) })
//	public void handle3(Gupiao gupiao) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，33333333 分区 gupiao:{0}", gupiao));
//		saveGupiao(gupiao);
//	}
//	@KafkaListener(id = "gupiao4", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiao}"), partitions = { "4" }) })
//	public void handle4(Gupiao gupiao) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，44444444 分区 gupiao:{0}", gupiao));
//		saveGupiao(gupiao);
//	}
//	@KafkaListener(id = "gupiao5", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiao}"), partitions = { "5" }) })
//	public void handle5(Gupiao gupiao) {
//		if ("0".equals(consumerOff)) return;
//		log.debug(MessageFormat.format("接收到消息，55555555 分区 gupiao:{0}", gupiao));
//		saveGupiao(gupiao);
//	}
//
//
//	private void saveGupiao(Gupiao gupiao){
//		try { gupiaoManager.saveGupiao(gupiao); }catch (Exception e){}
//	}
}
