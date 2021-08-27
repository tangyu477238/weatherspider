package cn.zifangsky.manager.impl;

import cn.zifangsky.config.KafkaConsole;
import cn.zifangsky.enums.KlineEnum;
import cn.zifangsky.manager.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    private GupiaoManagerImpl gupiaoManager;

    @Test
    public void listKzzData() {
        dongfangManager.listKzzData();
    }

    @Test
    public void listGupiaoData() {
        dongfangManager.listGupiaoData();
    }



    @Test
    public void getKline() {
        gupiaoManager.sysnKzzKlineAll(KlineEnum.K_30M.getId());
    }



}
