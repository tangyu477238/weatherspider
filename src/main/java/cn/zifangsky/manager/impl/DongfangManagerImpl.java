package cn.zifangsky.manager.impl;

import cn.zifangsky.common.ComUtil;
import cn.zifangsky.common.DateTimeUtil;
import cn.zifangsky.enums.DongfangEnum;
import cn.zifangsky.enums.KlineEnum;
import cn.zifangsky.login.StockUtil;
import cn.zifangsky.manager.DongfangManager;
import cn.zifangsky.manager.HttpClientManager;
import cn.zifangsky.manager.ProxyIpManager;
import cn.zifangsky.repository.GupiaoRepository;
import cn.zifangsky.spider.pipeline.DongfangKlinePipeline;
import cn.zifangsky.spider.gp.DongfangKlineSpider;
import cn.zifangsky.spider.gp.DongfangSpider;
import cn.zifangsky.spider.pipeline.GupiaoPipeline;
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

    @Resource
    private DongfangKlinePipeline dongfangKlinePipeline;

    @Resource
    private GupiaoPipeline gupiaoPipeline;

    @Resource
    private GupiaoRepository gupiaoRepository;

    @Resource
    private GupiaoManagerImpl gupiaoManager;
    @Resource
    private ProxyIpManager proxyIpManager;


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

    /***
     * 获取 url
     * @param bondId
     * @param period
     * @param isToday
     * @return
     */
    private String getUrl(String bondId, Integer period, boolean isToday){
        String beg = getBizdate(period);
        if (isToday){
            beg = DateTimeUtil.formatDateStr(new Date(),"yyyyMMdd");
        }
        int exchange_type =  StockUtil.isShenshi(bondId)  ? 0 : 1; //深/沪
        StringBuffer url = new StringBuffer("http://");
        url.append(System.currentTimeMillis())
                .append(DongfangEnum.KLINE.getUrl())
                .append("&klt=").append(period)
                .append("&fqt=1&secid=").append(exchange_type).append(".").append(bondId)
                .append("&beg=").append(beg)
                .append("&end=20500000&_=1618930055730");
        log.debug(url.toString());
        return url.toString();
    }

    /******
     * 根据股票编码获取数据
     * @param bondId 股票编码
     * @param period k线类型
     * @param isProxy 是否需要代理
     */
    @Override
    public void getKline(String bondId, Integer period, boolean isProxy, boolean isToday) {

        if (!isToday && gupiaoManager.getKlineMaxBizdate(bondId, period)){ //非当天运行,且最新一天有数据，则不进行
            return;
        }

        if (isToday && !ComUtil.isEmpty(gupiaoManager.getBeforeTime(bondId, period))){ //当天运行,且前一个时间段已同步，则不进行
            return;
        }

        Spider spider = OOSpider.create(new DongfangKlineSpider()) //添加请求解析
                .addPipeline(dongfangKlinePipeline); //数据处理
        if (isProxy){
            HttpClientDownloader httpClientDownloader = httpClientManager.getHttpClientDownloader(); //获取代理
            if (httpClientDownloader==null){
                return;
            }
            spider.setDownloader(httpClientDownloader);
        }
        spider.addUrl(getUrl(bondId, period, isToday)); //添加URL
//        log.debug(bondId+"---"+period+"--------开始请求------------------------"+DateTimeUtil.formatTimetoString(new Date()));
        spider.thread(1).run();
//        log.debug(bondId+"---"+period+"-------完成退出------------------------"+DateTimeUtil.formatTimetoString(new Date()));
    }



    @Override
    public void getKline(String bondId, Integer period, boolean isProxy) {
        getKline(bondId, period, isProxy, false);
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
