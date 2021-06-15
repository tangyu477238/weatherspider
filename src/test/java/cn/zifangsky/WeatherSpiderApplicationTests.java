package cn.zifangsky;

import cn.zifangsky.config.KafkaConsole;
import cn.zifangsky.manager.CrawlManager;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.XinlangManager;
import cn.zifangsky.manager.XueqiuManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WeatherSpiderApplicationTests {

	@Autowired
	KafkaConsole kafkaConsole;

	@Resource
	private CrawlManager crawlManager;

	@Resource
	private XueqiuManager xueqiuManager;

	@Resource
	private DongfangManager dongfangManager;

	@Resource
	private XinlangManager xinlangManager;

	@Test
	public void contextLoads() {

		kafkaConsole.edit("topic-proxyIp",6);

		kafkaConsole.edit("topic-gupiao",6);
		kafkaConsole.edit("topic-gupiaoGainian",6);
		kafkaConsole.edit("topic-gupiaoCodeKline",6);
		kafkaConsole.edit("topic-gupiaoKline",6);

	}

	@Test
	public void getJsl() {
		crawlManager.getDataJsl(); //可转债
	}

	@Test
	public void getXueqiu() {
		xueqiuManager.listGupiaoData(); //股票
	}

	@Test
	public void getXueqiu1M() {
		xueqiuManager.getDataXueqiuDetailKline("SZ128112", "1m",System.currentTimeMillis());

	}

	@Test
	public void getXueqiu5M() {
		xueqiuManager.getDataXueqiuDetailKline("SZ399006", "5m",System.currentTimeMillis());
	}

	@Test
	public void getXueqiu15M() {
		xueqiuManager.getDataXueqiuDetailKline("SZ128112", "15m",System.currentTimeMillis());
	}
	@Test
	public void getXueqiuDay() {
		xueqiuManager.getDataXueqiuDetailKline("SZ000001", "day",System.currentTimeMillis());
	}

	@Test
	public void getXinlang() {
		xinlangManager.getGupiaoGainian("");
	}


	@Test
	public void getDongfangDay() {
		dongfangManager.getKline("399006", "101",System.currentTimeMillis());
//		dongfangManager.getKline("399006", "5",System.currentTimeMillis());
	}




}
