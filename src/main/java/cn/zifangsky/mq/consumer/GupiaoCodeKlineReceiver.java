package cn.zifangsky.mq.consumer;

import cn.zifangsky.common.ExecutorProcessPool;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.impl.DongfangServiceImpl;
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

//	@Value("${mq.kline.today.off}")
//	private String klineTodayOff;

	@Resource
	private DongfangManager dongfangManager;

	@Resource
	private DongfangServiceImpl dongfangService;

	/**
	 * 接收消息并处理
	 */
	@KafkaListener(id = "gpcodeklineidx", topicPartitions = { @TopicPartition(topic = ("${mq.topicName.gupiaoCodeKline}"),
			partitions = { "0","1","2","3","4","5","6","7","8","9",
					"10","11","12","13","14","15","16","17","18","19"}) }, containerFactory = "batchContainerFactory")
	public void handle0(Gupiao gupiao) {
		if ("0".equals(klineOff)) {return;}
		try {
			Runnable run = new GupiaoCodeKlineReceiver.GupiaoKlineCodeRunnable(gupiao);
			ExecutorProcessPool.getInstance().executeByFixedThread(run);
		} catch (Exception e) {
			log.debug(e.toString());
		}
	}


	public class GupiaoKlineCodeRunnable implements Runnable{
		private Gupiao gupiao;
		public GupiaoKlineCodeRunnable(Gupiao gupiao){
			this.gupiao=gupiao;
		}
		@Override
		public void run(){
			log.info(MessageFormat.format("接收到消息，Receiver:{0}/{1}", gupiao.getSymbol(), gupiao.getPeriod()));
			boolean isToday = gupiao.getFollowers()==1 ? true : false; //当天数据获取
			dongfangService.getKine(gupiao.getSymbol(), gupiao.getPeriod(),true, isToday);
			return;
		}
	}


  
}
