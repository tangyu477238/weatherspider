package cn.zifangsky.test.spider;

import cn.zifangsky.manager.BizOrderManager;
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
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TestOrderSpider {

    @Resource(name = "bizOrderManager")
    private BizOrderManager bizOrderManager;




    @Test
    public void testTrainCrawl(){
        bizOrderManager.getOrder();
    }


}
