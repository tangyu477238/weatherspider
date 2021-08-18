package cn.zifangsky.test.jms;

import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.ProxyIp;
import cn.zifangsky.model.bo.ProxyIpBO;
import cn.zifangsky.mq.producer.CheckIPSender;
import cn.zifangsky.mq.producer.WeatherUpdateSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试通过Kafka发送和接收消息
 * @author zifangsky
 * @date 2018/6/20
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TestKafka {


    @Resource(name="proxyIpManager")
    private ProxyIpManager proxyIpManager;


	@Resource(name="checkIPSender")
	private CheckIPSender checkIPSender;



    /**
     * 测试更新代理IP
     */
	@Test
	public void testSend() throws InterruptedException {
		//1 查询数据库中所有代理IP
		List<ProxyIp> list = proxyIpManager.selectAll();

		if(list != null && list.size() > 0){
			//2 遍历并检测其可用性
		    list.forEach(proxyIp -> {
				ProxyIpBO proxyIpBO = new ProxyIpBO();
				proxyIpBO.setId(proxyIp.getId());
				proxyIpBO.setIp(proxyIp.getIp());
				proxyIpBO.setPort(proxyIp.getPort());
				proxyIpBO.setType(proxyIp.getType());
				proxyIpBO.setAddr(proxyIp.getAddr());
				proxyIpBO.setUsed(proxyIp.getUsed());
				proxyIpBO.setOther(proxyIp.getOther());
				proxyIpBO.setCheckType(ProxyIpBO.CheckIPType.UPDATE);

				//3 添加到队列中
				checkIPSender.send(proxyIpBO);
			});

		    //暂停线程，查看效果
		    Thread.sleep(1000 * 60);
		}

	}



}
