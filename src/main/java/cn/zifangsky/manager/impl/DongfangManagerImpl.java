package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.emuns.KlineEnum;
import cn.zifangsky.login.StockUtil;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.GupiaoManager;
import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.repository.GupiaoKlineRepository;
import cn.zifangsky.repository.GupiaoRepository;
import cn.zifangsky.spider.gp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service("dongfangManagerImpl")
public class DongfangManagerImpl implements DongfangManager {

    @Resource(name="httpClientManager")
    private HttpClientManager httpClientManager;

    @Resource(name="dongfangKlinePipeline")
    private DongfangKlinePipeline dongfangKlinePipeline;

    @Resource
    private GupiaoPipeline gupiaoPipeline;

    @Resource
    private GupiaoRepository gupiaoRepository;

    @Resource
    private GupiaoManagerImpl gupiaoManager;


    /****
     * 获取当天
     * 所有可转债清单
     */
    @Override
    public void listKzzData() {
        OOSpider.create(new DongfangSpider()).addPipeline(gupiaoPipeline)
                .addUrl("https://"+System.currentTimeMillis()+".push2.eastmoney.com/api/qt/clist/get?cb=jQuery1124009866857718349142_1628899426141&pn=1&pz=500&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=b:MK0354&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,f19,f20&_=1628899426142")
                .thread(1)
                .run();
    }

    /****
     * 获取当天
     * 所有股票清单
     */
    @Override
    public void listGupiaoData() {
        OOSpider.create(new DongfangSpider()).addPipeline(gupiaoPipeline)
                .addUrl("https://"+System.currentTimeMillis()+".push2.eastmoney.com/api/qt/clist/get?cb=jQuery112400669458161096197_1625868279098&pn=1&pz=5000&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,f19,f20&_=1625868279108")
                .thread(1)
                .run();
    }

    private String getBizdate(Integer period){
        int num = 500;
        if (period== KlineEnum.K_5M.getId()){
            num = Double.valueOf(Math.ceil(500/48)).intValue();
        } else if (period== KlineEnum.K_30M.getId()){
            num = Double.valueOf(Math.ceil(500/8)).intValue();
        } else if (period== KlineEnum.K_1D.getId()){
            num = 500;
        } else if (period== KlineEnum.K_1W.getId()){
            num = 500*5;
        }
        return gupiaoRepository.getBizDate(num).replace("-","");
    }

    /******
     * 根据股票编码获取数据
     * @param bondId 股票编码
     * @param period k线类型
     * @param isProxy 是否需要代理
     */
    @Override
    public void getKline(String bondId, Integer period, boolean isProxy, boolean isToday) {

        if (!isToday && gupiaoManager.getKlineMaxBizdate(bondId, period)){ //已存在,且非当天同步，则不在进行
            return;
        }

        if (isToday && !ComUtil.isEmpty(gupiaoManager.listBeforeTime(bondId, period))){ //已存在,且非当天同步，则不在进行
            return;
        }

        String beg = getBizdate(period);
        if (isToday){
            beg = DateTimeUtil.formatDateTimetoString(new Date(),"yyyyMMdd");
        }
        int exchange_type =  StockUtil.isShenshi(bondId)  ? 0 : 1; //深/沪
        StringBuffer url = new StringBuffer("http://"+System.currentTimeMillis()+".push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery112403780605306048155_1618930055627&fields1=f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6")
                .append("&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61&ut=7eea3edcaed734bea9cbfc24409ed989")
                .append("&klt="+period+"&fqt=1&secid="+exchange_type+"."+bondId+"&beg="+beg+"&end=20500000&_=1618930055730");
        log.debug(url.toString());
        Spider spider = OOSpider.create(new DongfangKlineSpider())
                .addPipeline(dongfangKlinePipeline).addUrl(url.toString());
        if (isProxy){
            HttpClientDownloader httpClientDownloader = httpClientManager.getHttpClientDownloader();
            if (httpClientDownloader==null){
                return;
            }
            spider.setDownloader(httpClientDownloader);
        }
        log.info(url.toString());
        spider.thread(1).run();
    }



    @Override
    public void getKline(String bondId, Integer period, boolean isProxy) {
        getKline(bondId, period, true, false);
    }

    @Override
    public void getKline(String bondId, Integer period) {
        getKline(bondId, period, true);
    }

    @Override
    public void getKlineDay(String bondId) {
        getKline(bondId, KlineEnum.K_1D.getId());
    }

    @Override
    public void getKline5M(String bondId) {
        getKline(bondId, KlineEnum.K_5M.getId());
    }


    @Override
    public void getToDayKline(String bondId) {
        getKline(bondId, KlineEnum.K_1D.getId(), true, true);
    }

    @Override
    public void getToDayKline5M(String bondId) {
        getKline(bondId, KlineEnum.K_5M.getId(), true, true);
    }
}
