package cn.zifangsky.manager.impl;

import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.manager.XinlangManager;
import cn.zifangsky.repository.GupiaoKlineRepository;
import cn.zifangsky.repository.XueqiuGupiaoRepository;
import cn.zifangsky.spider.gp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;

@Slf4j
@Service("xinlangManager")
public class XinlangManagerImpl implements XinlangManager {



    @Resource(name="httpClientManager")
    private HttpClientManager httpClientManager;



    @Resource(name="xinlangGainianPipeline")
    private XinlangGainianPipeline xinlangGainianPipeline;


    @Override
    public void getGupiaoGainian(String bondId) {
        OOSpider.create(new XinlangSpider()).addPipeline(xinlangGainianPipeline)
                .setDownloader(httpClientManager.getHttpClientDownloader())
                .addUrl("http://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpOtherInfo/stockid/"+bondId+"/menu_num/5.phtml")
                .thread(1)
                .run();
    }


}
