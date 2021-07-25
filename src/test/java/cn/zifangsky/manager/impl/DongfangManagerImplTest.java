package cn.zifangsky.manager.impl;

import cn.zifangsky.config.KafkaConsole;
import cn.zifangsky.login.StockUtil;
import cn.zifangsky.manager.*;
import cn.zifangsky.repository.GupiaoKlineRepository;
import cn.zifangsky.repository.GupiaoRepository;
import cn.zifangsky.spider.gp.DongfangGupiaoSpider;
import cn.zifangsky.spider.gp.DongfangKlinePipeline;
import cn.zifangsky.spider.gp.DongfangSpider;
import cn.zifangsky.spider.gp.GupiaoPipeline;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DongfangManagerImplTest  {

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
    public void listGupiaoData() {
        dongfangManager.listGupiaoData();
    }


    @Test
    public void getKline() {
		dongfangManager.getKline("000002", "101",false);
    }



}
