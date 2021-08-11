package cn.zifangsky.mq.producer;

import cn.zifangsky.model.bo.ProxyIpBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Random;

@Slf4j
@Component("checkIPSender")
public class CheckIPSender {

    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;

	/**
	 * 发送检测某个代理IP可用性的消息到指定队列
	 * @param topicName  队列名称
	 * @param proxyIpBO  消息内容
	 */
	public void send(String topicName,ProxyIpBO proxyIpBO){
        log.debug(MessageFormat.format("开始向Kafka推送数据，topicName：{0}，代理IP：{1}",topicName, proxyIpBO));

        try {
            String p = String.valueOf(new Random().nextInt(100) + 1);
            ProducerRecord producerRecord = new ProducerRecord<String, Object>(topicName, p , proxyIpBO);
            kafkaTemplate.send(producerRecord);
//            kafkaTemplate.send(topicName, proxyIpBO);
            log.debug("推送数据成功！");
        } catch (Exception e) {
            log.error(MessageFormat.format("推送数据出错，topicName:{0},代理IP:{1}"
                    ,topicName,proxyIpBO),e);
        }
	}

}
