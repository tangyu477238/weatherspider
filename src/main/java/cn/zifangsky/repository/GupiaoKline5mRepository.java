package cn.zifangsky.repository;

import cn.zifangsky.model.GupiaoKline5m;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface GupiaoKline5mRepository extends JpaRepository<GupiaoKline5m,Integer> {

    List<GupiaoKline5m> findBySymbolOrderByBizDate(String bondId);

    GupiaoKline5m findBySymbolAndPeriodAndTimestamp(String bondId, String period, Date date);

    GupiaoKline5m findBySymbolAndPeriodAndBizDate(String bondId, String period, String bizDate);

    @Query(value = "select * from gupiao_kline_5m where symbol = ?1 and biz_date>='2020-01-01' order by biz_date desc  LIMIT 0, ?2 ", nativeQuery = true)
    List<GupiaoKline5m> getSymbolTop(String bondId, int topNum);

}
