package cn.zifangsky.mq.producer;

import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.model.Gupiao;
import cn.zifangsky.model.GupiaoKline;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Random;

@Slf4j
@Component("gupiaoKlineSender")
public class GupiaoKlineSender {


    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;

    @Value("${mq.topicName.gupiaoKline}")
    private String topicName;

	/**
	 * 发送GupiaoKline可用性的消息到指定队列
	 * @param gupiaoKline  消息内容
	 */
	public void send(BaseGupiaoKline gupiaoKline){

        try {
            String p = String.valueOf(new Random().nextInt(100) + 1);
            ProducerRecord producerRecord = new ProducerRecord<String, Object>(topicName, p , gupiaoKline);
            kafkaTemplate.send(producerRecord);
        } catch (Exception e) {
            log.error(MessageFormat.format("推送数据出错，topicName:{0},Kline:{1}"
                    ,topicName, gupiaoKline),e);
        }
	}

}
