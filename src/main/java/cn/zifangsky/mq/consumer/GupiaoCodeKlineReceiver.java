package cn.zifangsky.mq.consumer;

import cn.zifangsky.manager.DongfangManager;
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
	@KafkaListener(id = "gpcodeklineidx", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"),
			partitions = { "0","1","2","3","4","5","6","7","8","9",
					"10","11","12","13","14"}) }, containerFactory = "batchContainerFactory")
	public void handle0(Gupiao gupiao) {
		if ("0".equals(klineOff)) return;
		log.info(MessageFormat.format("接收到消息，gupiaoCodeKlineReceiver:{0}/{1}", gupiao.getSymbol(), gupiao.getPeriod()));
		getKlineData(gupiao);
	}

	private void getKlineData(Gupiao gupiao){
		try {
			if (gupiao.getFollowers()==1){
				dongfangManager.getKline(gupiao.getSymbol(), gupiao.getPeriod(),true,true);
			} else {
				dongfangManager.getKline(gupiao.getSymbol(), gupiao.getPeriod());
			}
		}catch (Exception e){log.debug(e.toString());}
	}


//	private void getKlineData(Gupiao gupiao){
//		try {
//			Runnable run = new GupiaoCodeKlineReceiver.GupiaoKlineCodeRunnable(gupiao);
//			ExecutorProcessPool.getInstance().executeByCustomThread(run);
//		}catch (Exception e){log.debug(e.toString());}
//	}
//
//	public class GupiaoKlineCodeRunnable implements Runnable{
//		private Gupiao gupiao;
//		public GupiaoKlineCodeRunnable(Gupiao gupiao){
//			this.gupiao=gupiao;
//		}
//		@Override
//		public void run(){
//			if (gupiao.getFollowers()==1){
//				dongfangManager.getKline(gupiao.getSymbol(), gupiao.getPeriod(),true,true);
//			} else {
//				dongfangManager.getKline(gupiao.getSymbol(), gupiao.getPeriod());
//			}
//
//		}
//	}


  
}
