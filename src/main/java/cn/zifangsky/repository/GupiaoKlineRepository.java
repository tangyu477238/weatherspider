package cn.zifangsky.repository;

import cn.zifangsky.model.GupiaoKline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface GupiaoKlineRepository extends JpaRepository<GupiaoKline,Integer> {

    GupiaoKline findBySymbol(String bondId);

    GupiaoKline findBySymbolAndPeriodAndTimestamp(String bondId, String period, Date date);

    GupiaoKline findBySymbolAndPeriodAndBizDate(String bondId, String period, String bizDate);



    @Query(value = "select COUNT(1) as sl " +
            " from gupiao_kline k " +
            " inner join ( " +
            " select MAX(biz_date) as biz_date from biz_calendar " +
            " where holiday = 1 and biz_date<=date_format(now(),'%Y-%m-%d') " +
            " ) t on k.biz_date >= t.biz_date " +
            " where k.symbol=?1 and period =?2 ", nativeQuery = true)
    Integer getKlineMaxBizdate(String bondId, String period);


    @Query(value = "select COUNT(1) as sl " +
            " from gupiao_kline_5m k " +
            " inner join ( " +
            " select MAX(biz_date) as biz_date from biz_calendar " +
            " where holiday = 1 and biz_date<=date_format(now(),'%Y-%m-%d') " +
            " ) t on k.biz_date >= t.biz_date " +
            " where k.symbol=?1 and period =?2 ", nativeQuery = true)
    Integer getKlineM5MaxBizdate(String bondId, String period);



}
