package cn.zifangsky.manager.impl;

import cn.zifangsky.login.StockUtil;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.repository.GupiaoKlineRepository;
import cn.zifangsky.repository.GupiaoRepository;
import cn.zifangsky.spider.gp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;

@Slf4j
@Service("dongfangManagerImpl")
public class DongfangManagerImpl implements DongfangManager {

    @Resource
    private GupiaoKlineRepository gupiaoKlineRepository;

    @Resource
    private GupiaoRepository gupiaoRepository;


    @Resource(name="httpClientManager")
    private HttpClientManager httpClientManager;

    @Resource(name="dongfangKlinePipeline")
    private DongfangKlinePipeline dongfangKlinePipeline;

    @Resource
    private GupiaoPipeline gupiaoPipeline;



    /****
     * 获取当天
     * 所有可转债清单
     */
    @Override
    public void listKzzData() {
        OOSpider.create(new DongfangKzzSpider()).addPipeline(gupiaoPipeline)
                .setDownloader(httpClientManager.getHttpClientDownloader())
                .addUrl("https://datacenter-web.eastmoney.com/api/data/v1/get?callback=jQuery1123033215716759271463_1628607150447&sortColumns=PUBLIC_START_DATE&sortTypes=-1&pageSize=5000&pageNumber=1&reportName=RPT_BOND_CB_LIST&columns=SECURITY_CODE&quoteColumns=f2&source=WEB&client=WEB")
                .thread(1)
                .run();
    }

    /****
     * 获取当天
     * 所有股票清单
     */
    @Override
    public void listGupiaoData() {
        OOSpider.create(new DongfangGupiaoSpider()).addPipeline(gupiaoPipeline)
				.setDownloader(httpClientManager.getHttpClientDownloader())
                .addUrl("https://14.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112400669458161096197_1625868279098&pn=1&pz=5000&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23&fields=f12,f14&_=1625868279108")
                .thread(1)
                .run();
    }

    /******
     * 根据股票编码获取数据
     * @param bondId 股票编码
     * @param period k线类型
     * @param flag 是否需要代理
     */
    @Override
    public void getKline(String bondId, String period, boolean flag) {
        int exchange_type =  StockUtil.isShenshi(bondId)  ? 0 : 1; //深/沪
        String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery112403780605306048155_1618930055627&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6" +
                "&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&ut=7eea3edcaed734bea9cbfc24409ed989" +
                "&klt="+period+"&fqt=1&secid="+exchange_type+"."+bondId+"&beg=20160101&end=20500000&_=1618930055730";
        log.info(url);
        Spider spider = OOSpider.create(new DongfangSpider()).addPipeline(dongfangKlinePipeline).addUrl(url);
        if (flag){
            spider.setDownloader(httpClientManager.getHttpClientDownloader());
        }
        spider.thread(1).run();
    }




}
