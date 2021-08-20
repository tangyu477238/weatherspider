package cn.zifangsky.repository;

import cn.zifangsky.model.GupiaoKline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface GupiaoKlineRepository extends JpaRepository<GupiaoKline,Integer> {



    @Query(value = "select * from gupiao_kline where symbol=?1 and period =?2 and biz_date =?3 ", nativeQuery = true)
    GupiaoKline getKline(String bondId, Integer period, String bizDate);

    @Query(value = "select * from gupiao_kline_5m where symbol=?1 and period =?2 and biz_date =?3 ", nativeQuery = true)
    GupiaoKline getKline5m(String bondId, Integer period, String bizDate);

    @Query(value = "select * from gupiao_kline_15m where symbol=?1 and period =?2 and biz_date =?3 ", nativeQuery = true)
    GupiaoKline getKline15m(String bondId, Integer period, String bizDate);

    @Query(value = "select * from gupiao_kline_30m where symbol=?1 and period =?2 and biz_date =?3 ", nativeQuery = true)
    GupiaoKline getKline30m(String bondId, Integer period, String bizDate);

    @Query(value = "select * from gupiao_kline_60m where symbol=?1 and period =?2 and biz_date =?3 ", nativeQuery = true)
    GupiaoKline getKline60m(String bondId, Integer period, String bizDate);

    @Query(value = "select * from gupiao_kline_120m where symbol=?1 and period =?2 and biz_date =?3 ", nativeQuery = true)
    GupiaoKline getKline120m(String bondId, Integer period, String bizDate);






    @Query(value = "select COUNT(1) as sl " +
            " from gupiao_kline k " +
            " inner join v_syn_max_bizdate t on k.biz_date >= t.biz_date " +
            " where k.symbol=?1 and period =?2 ", nativeQuery = true)
    Integer getKlineMaxBizdate(String bondId, Integer period);


    @Query(value = "select COUNT(1) as sl " +
            " from gupiao_kline_5m k " +
            " inner join v_syn_max_bizdate t on k.biz_date >= t.biz_date " +
            " where k.symbol=?1 and period =?2 ", nativeQuery = true)
    Integer getKline5mMaxBizdate(String bondId, Integer period);


    @Query(value = "select COUNT(1) as sl " +
            " from gupiao_kline_30m k " +
            " inner join v_syn_max_bizdate t on k.biz_date >= t.biz_date " +
            " where k.symbol=?1 and period =?2 ", nativeQuery = true)
    Integer getKline30mMaxBizdate(String bondId, Integer period);
}
