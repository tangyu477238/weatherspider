package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.manager.XueqiuManager;
import cn.zifangsky.model.GupiaoKline;
import cn.zifangsky.repository.GupiaoKlineRepository;
import cn.zifangsky.repository.GupiaoRepository;
import cn.zifangsky.spider.gp.XueqiuGupiaoKlinePipeline;
import cn.zifangsky.spider.gp.GupiaoPipeline;
import cn.zifangsky.spider.gp.XueqiuSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;

@Slf4j
@Service
public class XueqiuManagerImpl implements XueqiuManager {

    @Resource(name = "gupiaoKlineRepository")
    private GupiaoKlineRepository gupiaoKlineRepository;

    @Resource
    private GupiaoRepository gupiaoRepository;


    @Resource(name="httpClientManager")
    private HttpClientManager httpClientManager;

    @Resource(name="xueqiuGupiaoKlinePipeline")
    private XueqiuGupiaoKlinePipeline xueqiuGupiaoKlinePipeline;

    @Autowired
    private GupiaoPipeline gupiaoPipeline;


    /****
     * 获取当天
     * 所有股票清单
     */
    @Override
    public void listGupiaoData() {
        OOSpider.create(new XueqiuSpider()).addPipeline(gupiaoPipeline)
				.setDownloader(httpClientManager.getHttpClientDownloader())
                .addUrl("https://xueqiu.com/service/v5/stock/screener/quote/list?page=1&size=5000&order=desc&orderby=percent&order_by=percent&market=CN&type=sh_sz&_=1606612746025")
                .thread(1)
                .run();
    }

    /******
     * 根据股票编码获取数据
     * @param bondId 股票编码
     * @param period k线类型
     */
    @Override
    public void getDataXueqiuDetailKline(String bondId, String period, long timestamp) {
        String count = getCount(period); //记录数
        String url = "https://stock.xueqiu.com/v5/stock/chart/kline.json?symbol="+bondId+"&begin="+timestamp+"&period="+period+"&type=before&count="+count+"&indicator=kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance";
        log.debug(url);
        OOSpider.create(new XueqiuSpider()).addPipeline(xueqiuGupiaoKlinePipeline)
                .setDownloader(httpClientManager.getHttpClientDownloader())
                .addUrl(url)
                .thread(1)
                .run();
    }

    private String getCount(String period){
        if (period.equals("day")){
            return "-800";
        }
        if (period.indexOf("m")>-1){
            return "-500";
        }
        return "-50";
    }



    /***
     * 股票k线数据存储
     * @param gupiaoKline
     */
    @Override
    public void saveXueqiuKline(GupiaoKline gupiaoKline) {
        try {
            GupiaoKline kline = gupiaoKlineRepository.findBySymbolAndPeriodAndBizDate(gupiaoKline.getSymbol(),
                    gupiaoKline.getPeriod(), gupiaoKline.getBizDate());
            if (ComUtil.isEmpty(kline)){
                gupiaoKlineRepository.save(gupiaoKline);
            }
        } catch (Exception e){
            log.debug(e.getMessage());
        }
    }


}
