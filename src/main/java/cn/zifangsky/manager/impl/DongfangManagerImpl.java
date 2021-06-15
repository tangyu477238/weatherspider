package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.model.XueqiuGupiao;
import cn.zifangsky.model.GupiaoKline;
import cn.zifangsky.repository.GupiaoKlineRepository;
import cn.zifangsky.repository.XueqiuGupiaoRepository;
import cn.zifangsky.spider.gp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;

@Slf4j
@Service("dongfangManagerImpl")
public class DongfangManagerImpl implements DongfangManager {

    @Resource
    private GupiaoKlineRepository gupiaoKlineRepository;

    @Resource
    private XueqiuGupiaoRepository xueqiuGupiaoRepository;


    @Resource(name="httpClientManager")
    private HttpClientManager httpClientManager;

    @Resource(name="dongfangKlinePipeline")
    private DongfangKlinePipeline dongfangKlinePipeline;

    @Resource(name="xueqiuGupiaoPipeline")
    private XueqiuGupiaoPipeline xueqiuGupiaoPipeline;


    /****
     * 获取当天
     * 所有股票清单
     */
    @Override
    public void listGupiaoData() {
        OOSpider.create(new XueqiuSpider()).addPipeline(xueqiuGupiaoPipeline)
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
    public void getKline(String bondId, String period, long timestamp) {
        String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery112403780605306048155_1618930055627&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6" +
                "&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&ut=7eea3edcaed734bea9cbfc24409ed989" +
                "&klt="+period+"&fqt=1&secid=0."+bondId+"&beg=0&end=20500000&_=1618930055730";
        log.info(url);
        OOSpider.create(new DongfangSpider()).addPipeline(dongfangKlinePipeline)
                .setDownloader(httpClientManager.getHttpClientDownloader())
                .addUrl(url)
                .thread(1)
                .run();
    }




    /***
     * 股票数据存储
     * @param gupiao
     */
    @Override
    public void saveGupiao(XueqiuGupiao gupiao) {
        XueqiuGupiao kzz1 = xueqiuGupiaoRepository.findBySymbol(gupiao.getSymbol());
        if (kzz1 != null){
            gupiao.setId(kzz1.getId());
        }
        xueqiuGupiaoRepository.save(gupiao);



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
            log.info(e.getMessage());
        }
    }


}
