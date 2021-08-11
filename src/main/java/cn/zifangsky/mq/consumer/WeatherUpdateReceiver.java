package cn.zifangsky.mq.consumer;

import cn.zifangsky.manager.CrawlManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

@Component("weatherUpdateReceiver")
@Slf4j
public class WeatherUpdateReceiver{
	@Value("${mq.consumer.off}")
	private String consumerOff;

	@Resource(name="crawlManager")
	private CrawlManager crawlManager;

	/**
	 * 接收消息并处理
	 * @param list
	 */
	@KafkaListener(topics = {"${mq.topicName.weather}"},groupId = "group1")
	public void handle(List<String> list){

	    list.forEach(stationCode -> {
            log.debug(MessageFormat.format("接收到消息，城镇CODE:{0}", stationCode));

            //更新天气
//            crawlManager.weatherCrawl(stationCode);
        });

	}

}
