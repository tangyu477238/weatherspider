package cn.zifangsky.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Random;

@Slf4j
@Component
public class GupiaoGainianSender {

//    @Autowired
//    private KafkaTemplate<Object,Object> kafkaTemplate;
//
//    @Value("${mq.topicName.gupiaoGainian}")
//    private String topicName;
//
//	/**
//	 * 发送X可用性的消息到指定队列
//	 * @param gupiao  消息内容
//	 */
//	public void send(String gupiao){
//        log.debug(MessageFormat.format("开始向Kafka推送数据，topicName：{0}，gupiaoGainian：{1}",topicName, gupiao));
//
//        try {
//            String p = String.valueOf(new Random().nextInt(100) + 1);
//            ProducerRecord producerRecord = new ProducerRecord<String, Object>(topicName, p , gupiao);
//            kafkaTemplate.send(producerRecord);
//            log.debug("推送数据成功！");
//        } catch (Exception e) {
//            log.error(MessageFormat.format("推送数据出错，topicName:{0},gupiaoGainian:{1}"
//                    ,topicName,gupiao),e);
//        }
//	}

}
