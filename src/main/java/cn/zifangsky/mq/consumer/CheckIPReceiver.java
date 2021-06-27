package cn.zifangsky.mq.consumer;

import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.ProxyIp;
import cn.zifangsky.model.bo.ProxyIpBO;
import cn.zifangsky.spider.CheckIPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Component("checkIPReceiver")
@Slf4j
public class CheckIPReceiver {

	@Value("${mq.consumer.off}")
	private String consumerOff;

	@Resource(name = "proxyIpManager")
	private ProxyIpManager proxyIpManager;


//	@KafkaListener(id = "id0", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.checkIP}"), partitions = { "0" }) })
//	public void handle0(ProxyIpBO proxyIpBO) {
//		log.info(MessageFormat.format("接收到消息，000000000 分区 代理IP:{0}", proxyIpBO));
//		runCheckIp(proxyIpBO);
//	}
//
//	@KafkaListener(id = "id1", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.checkIP}"), partitions = { "1" }) })
//	public void handle1(ProxyIpBO proxyIpBO) {
//		log.info(MessageFormat.format("接收到消息，11111111 分区 代理IP:{0}", proxyIpBO));
//		runCheckIp(proxyIpBO);
//	}
//	@KafkaListener(id = "id2", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.checkIP}"), partitions = { "2" }) })
//	public void handle2(ProxyIpBO proxyIpBO) {
//		log.info(MessageFormat.format("接收到消息，22222222 分区 代理IP:{0}", proxyIpBO));
//		runCheckIp(proxyIpBO);
//	}
//	@KafkaListener(id = "id3", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.checkIP}"), partitions = { "3" }) })
//	public void handle3(ProxyIpBO proxyIpBO) {
//		log.info(MessageFormat.format("接收到消息，33333333 分区 代理IP:{0}", proxyIpBO));
//		runCheckIp(proxyIpBO);
//	}
//	@KafkaListener(id = "id4", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.checkIP}"), partitions = { "4" }) })
//	public void handle4(ProxyIpBO proxyIpBO) {
//		log.info(MessageFormat.format("接收到消息，44444444 分区 代理IP:{0}", proxyIpBO));
//		runCheckIp(proxyIpBO);
//	}
//	@KafkaListener(id = "id5", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.checkIP}"), partitions = { "5" }) })
//	public void handle5(ProxyIpBO proxyIpBO) {
//		log.info(MessageFormat.format("接收到消息，55555555 分区 代理IP:{0}", proxyIpBO));
//		runCheckIp(proxyIpBO);
//	}


	private void runCheckIp(ProxyIpBO proxyIpBO){

		// 根据该IP是待入库的新IP或者数据库中的旧IP分两种情况判断
		if (proxyIpBO.getCheckType() == ProxyIpBO.CheckIPType.ADD) {
			// 1 查询该IP是否已存在
			ProxyIp oldIP = proxyIpManager.selectByIPPort(proxyIpBO.getIp(), proxyIpBO.getPort());
			if (oldIP != null){
				return ;
			}
			if (CheckIPUtils.checkValidIP(proxyIpBO.getIp(), proxyIpBO.getPort())) {
				// 2如果不存在则插入数据
				proxyIpBO.setUsed(false);
				try {
					proxyIpManager.insert(proxyIpBO);
				} catch (Exception e) {
					log.info("");
				}
			}
		} else if (proxyIpBO.getCheckType() == ProxyIpBO.CheckIPType.UPDATE) {
			log.info("检查ip的有效性");
			// 不能使用则删除
			if (!CheckIPUtils.checkValidIP(proxyIpBO.getIp(), proxyIpBO.getPort())) {
				proxyIpManager.deleteByPrimaryKey(proxyIpBO.getId());
			}
		}
	}
}
