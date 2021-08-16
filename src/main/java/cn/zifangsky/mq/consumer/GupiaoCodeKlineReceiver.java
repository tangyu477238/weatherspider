package cn.zifangsky.mq.consumer;

import cn.zifangsky.common.ExecutorProcessPool;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.model.BaseGupiaoKline;
import cn.zifangsky.model.Gupiao;
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
	private String klineOff;

	@Resource
	private DongfangManager dongfangManager;



	/**
	 * 接收消息并处理
	 */


	@KafkaListener(id = "gpcodeklineid0", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "0" }) })
	public void handle0(Gupiao gupiao) {
		if ("0".equals(klineOff)) return;
		log.debug(MessageFormat.format("接收到消息，000000000 分区 gupiaoCodeKlineReceiver:{0}", gupiao));
		getKlineData(gupiao);
	}
	@KafkaListener(id = "gpcodeklineid1", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "1" }) })
	public void handle1(Gupiao gupiao) {
		if ("0".equals(klineOff)) return;
		log.debug(MessageFormat.format("接收到消息，11111111 分区  gupiaoCodeKlineReceiver:{0}", gupiao));
		getKlineData(gupiao);
	}
	@KafkaListener(id = "gpcodeklineid2", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "2" }) })
	public void handle2(Gupiao gupiao) {
		if ("0".equals(klineOff)) return;
		log.debug(MessageFormat.format("接收到消息，22222222 分区  gupiaoCodeKlineReceiver:{0}", gupiao));
		getKlineData(gupiao);
	}
	@KafkaListener(id = "gpcodeklineid3", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "3" }) })
	public void handle3(Gupiao gupiao) {
		if ("0".equals(klineOff)) return;
		log.debug(MessageFormat.format("接收到消息，33333333 分区  gupiaoCodeKlineReceiver:{0}", gupiao));
		getKlineData(gupiao);
	}
	@KafkaListener(id = "gpcodeklineid4", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "4" }) })
	public void handle4(Gupiao gupiao) {
		if ("0".equals(klineOff)) return;
		log.debug(MessageFormat.format("接收到消息，44444444 分区  gupiaoCodeKlineReceiver:{0}", gupiao));
		getKlineData(gupiao);
	}
	@KafkaListener(id = "gpcodeklineid5", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"), partitions = { "5" }) })
	public void handle5(Gupiao gupiao) {
		if ("0".equals(klineOff)) return;
		log.debug(MessageFormat.format("接收到消息，55555555 分区  gupiaoCodeKlineReceiver:{0}", gupiao));
		getKlineData(gupiao);
	}


	private void getKlineData(Gupiao gupiao){
		try {
			Runnable run = new GupiaoCodeKlineReceiver.GupiaoKlineCodeRunnable(gupiao);
			ExecutorProcessPool.getInstance().executeByCustomThread(run);
		}catch (Exception e){log.debug(e.toString());}
	}

	public class GupiaoKlineCodeRunnable implements Runnable{
		private Gupiao gupiao;
		public GupiaoKlineCodeRunnable(Gupiao gupiao){
			this.gupiao=gupiao;
		}
		@Override
		public void run(){
			dongfangManager.getKline(gupiao.getSymbol(), gupiao.getPeriod());
		}
	}


  
}
