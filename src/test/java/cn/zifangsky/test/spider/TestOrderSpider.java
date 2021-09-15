package cn.zifangsky.test.spider;

import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.manager.BizOrderManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 测试基于WebMagic框架的爬虫效果
 * @author zifangsky
 * @date 2018/6/21
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class TestOrderSpider {

    @Resource(name = "bizOrderManager")
    private BizOrderManager bizOrderManager;




    @Test
    public void testTrainCrawl(){
        for (int i = 0;i<30;i++){
            String bizDate = DateTimeUtil.formatDateStr(DateTimeUtil.addDays(new Date(),-1*i),DateTimeUtil.FMT_yyyyMMdd);
            try {
                bizOrderManager.getOrder(bizDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try { Thread.sleep(1000);} catch (InterruptedException ie){}//n为毫秒数
        }
    }


}
