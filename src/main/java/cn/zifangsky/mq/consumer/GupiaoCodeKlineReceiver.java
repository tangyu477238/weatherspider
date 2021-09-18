package cn.zifangsky.mq.consumer;

import cn.zifangsky.common.ExecutorProcessPool;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.model.Gupiao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Component
@Slf4j
public class GupiaoCodeKlineReceiver {

	@Value("${mq.kline.off}")
	private String klineOff;

	@Value("${mq.kline.today.off}")
	private String klineTodayOff;

	@Resource
	private DongfangManager dongfangManager;

	/**
	 * 接收消息并处理
	 */
	@KafkaListener(id = "gpcodeklineidx", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"),
			partitions = { "0","1","2","3","4","5","6","7","8","9",
					"10","11","12","13","14","15","16","17","18","19"}) }, containerFactory = "batchContainerFactory")
	public void handle0(Gupiao gupiao) {
		try {
			Runnable run = new GupiaoCodeKlineReceiver.GupiaoKlineCodeRunnable(gupiao);
			ExecutorProcessPool.getInstance().executeByFixedThread(run);
		}catch (Exception e){log.debug(e.toString());}
	}


	public class GupiaoKlineCodeRunnable implements Runnable{
		private Gupiao gupiao;
		public GupiaoKlineCodeRunnable(Gupiao gupiao){
			this.gupiao=gupiao;
		}
		@Override
		public void run(){
			if ("1".equals(klineOff) && gupiao.getFollowers()!=1){
				log.info(MessageFormat.format("接收到消息，gupiaoCodeKlineReceiver:{0}/{1}", gupiao.getSymbol(), gupiao.getPeriod()));
				dongfangManager.getKline(gupiao.getSymbol(), gupiao.getPeriod());
				return;
			}
			if ("1".equals(klineTodayOff) && gupiao.getFollowers()==1){
				dongfangManager.getKline(gupiao.getSymbol(), gupiao.getPeriod(),true,true);
				return;
			}

		}
	}


  
}
