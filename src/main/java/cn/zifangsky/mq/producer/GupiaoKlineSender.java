package cn.zifangsky.mq.producer;

import cn.zifangsky.model.XueqiuGupiaoKline;
import cn.zifangsky.model.bo.ProxyIpBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Random;

@Slf4j
@Component("gupiaoKlineSender")
public class GupiaoKlineSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(GupiaoKlineSender.class);

    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;

    @Value("${mq.topicName.gupiaoKline}")
    private String topicName;

	/**
	 * 发送XueqiuGupiaoKline可用性的消息到指定队列
	 * @param xueqiuGupiaoKline  消息内容
	 */
	public void send(XueqiuGupiaoKline xueqiuGupiaoKline){
        LOGGER.info(MessageFormat.format("开始向Kafka推送数据，topicName：{0}，Kline：{1}",topicName, xueqiuGupiaoKline));

        try {
            String p = String.valueOf(new Random().nextInt(100) + 1);
            ProducerRecord producerRecord = new ProducerRecord<String, Object>(topicName, p , xueqiuGupiaoKline);
            kafkaTemplate.send(producerRecord);
            LOGGER.info("推送数据成功！");
        } catch (Exception e) {
            LOGGER.error(MessageFormat.format("推送数据出错，topicName:{0},Kline:{1}"
                    ,topicName,xueqiuGupiaoKline),e);
        }
	}

}
