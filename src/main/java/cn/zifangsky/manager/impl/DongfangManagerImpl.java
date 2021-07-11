package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.login.StockUtil;
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

//    @Resource(name="xueqiuGupiaoPipeline")
//    private XueqiuGupiaoPipeline xueqiuGupiaoPipeline;


    /****
     * 获取当天
     * 所有股票清单
     */
    @Override
    public void listGupiaoData() {
        OOSpider.create(new DongfangSpider()).addPipeline(dongfangKlinePipeline)
				.setDownloader(httpClientManager.getHttpClientDownloader())
                .addUrl("http://14.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112400669458161096197_1625868279098&pn=1&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1625868279108")
                .thread(3)
                .run();
    }

    /******
     * 根据股票编码获取数据
     * @param bondId 股票编码
     * @param period k线类型
     */
    @Override
    public void getKline(String bondId, String period, boolean flag) {
        int exchange_type =  StockUtil.isShenshi(bondId)  ? 0 : 1; //深/沪

        String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery112403780605306048155_1618930055627&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6" +
                "&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&ut=7eea3edcaed734bea9cbfc24409ed989" +
                "&klt="+period+"&fqt=1&secid="+exchange_type+"."+bondId+"&beg=0&end=20500000&_=1618930055730";
        log.info(url);
        if (flag){
            OOSpider.create(new DongfangSpider()).addPipeline(dongfangKlinePipeline)
                    .addUrl(url)
                    .thread(1)
                    .run();
            return;
        }
        OOSpider.create(new DongfangSpider()).addPipeline(dongfangKlinePipeline)
                .setDownloader(httpClientManager.getHttpClientDownloader())
                .addUrl(url)
                .thread(3)
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
