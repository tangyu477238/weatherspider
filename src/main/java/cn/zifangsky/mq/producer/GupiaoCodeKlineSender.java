package cn.zifangsky.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Random;


/*****
 * 通过编码
 * 去找对应的数据
 */
@Slf4j
@Component("gupiaoCodeKlineSender")
public class GupiaoCodeKlineSender {


    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;

    @Value("${mq.topicName.gupiaoCodeKline}")
    private String topicName;

	/**
	 * 发送XueqiuGupiaoKline可用性的消息到指定队列
	 * @param code  消息内容
	 */
	public void send(String code){
        log.debug(MessageFormat.format("开始向Kafka推送数据，topicName：{0}，Kline：{1}",topicName, code));

        try {
            String p = String.valueOf(new Random().nextInt(100) + 1);
            ProducerRecord producerRecord = new ProducerRecord<String, Object>(topicName, p , code);
            kafkaTemplate.send(producerRecord);
            log.debug("推送数据成功！");
        } catch (Exception e) {
            log.error(MessageFormat.format("推送数据出错，topicName:{0},Kline:{1}"
                    ,topicName,code),e);
        }
	}

}
