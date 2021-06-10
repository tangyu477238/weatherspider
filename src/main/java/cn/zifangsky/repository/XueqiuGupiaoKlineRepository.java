package cn.zifangsky.repository;

import cn.zifangsky.model.XueqiuGupiaoKline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface XueqiuGupiaoKlineRepository extends JpaRepository<XueqiuGupiaoKline,Integer> {

    XueqiuGupiaoKline findBySymbol(String bondId);

    XueqiuGupiaoKline findBySymbolAndPeriodAndTimestamp(String bondId, String period, Date date);

    XueqiuGupiaoKline findBySymbolAndPeriodAndBizDate(String bondId, String period, String bizDate);

}
