package cn.zifangsky.test.spider;

import cn.zifangsky.manager.CrawlManager;
import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.model.bo.ProxyIpBO;
import cn.zifangsky.spider.CheckIPUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * 测试基于WebMagic框架的爬虫效果
 * @author zifangsky
 * @date 2018/6/21
 * @since 1.0.0
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TestSpider{
	@Resource(name="crawlManager")
	private CrawlManager crawlManager;

    @Resource(name = "proxyIpManager")
    private ProxyIpManager proxyIpManager;

    @Resource
    private CheckIPUtils checkIPUtils;



    /**
     * 测试检测代理IP是否可用
     * @author zifangsky
     * @date 2018/6/21 14:08
     * @since 1.0.0
     */
    @Test
	public void testCheckProxyIp(){
        ProxyIpBO proxyIpBO = new ProxyIpBO();
        proxyIpBO.setIp("106.45.104.88");
        proxyIpBO.setPort(3256);

        log.info(checkIPUtils.checkValidIP(proxyIpBO.getIp(), proxyIpBO.getPort())+"");

    }
    
    /**
     * 测试通过接口1获取代理IP
     * @author zifangsky
     * @date 2018/6/21 14:13
     * @since 1.0.0
     */
    @Test
    public void crawlProxyIp1(){

        crawlManager.proxyIPCrawl();
    }



    @Test
    public void testTrainCrawl(){
//        TrainInfo trainInfo = new TrainInfo();
//        trainInfo.setTrainNum("G421");
//        List<TrainInfo> list = trainInfoMapper.listTrainInfo(trainInfo);
//        for (int i = 0 ;i<list.size();i++){
//            if (i < list.size()-1) {
//                TrainInfo trainInfo1 = list.get(i);
//                TrainInfo trainInfo2 = list.get(i + 1);
//                String fromStationCode = StationUtil.getStationCode(trainInfo1.getStationName());
//                String toStationCode = StationUtil.getStationCode(trainInfo2.getStationName());
//                crawlManager.trainCrawl("2020-09-25", fromStationCode, toStationCode);
//            }
//        }


    }


}
