package cn.zifangsky.repository;

import cn.zifangsky.model.Gupiao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GupiaoRepository extends JpaRepository<Gupiao,Integer> {

    Gupiao findBySymbol(String bondId);

    @Query(value = "select * from gupiao where symbol like '11%' or  symbol like '12%' ", nativeQuery = true)
    List<Gupiao> listkzz();

    @Query(value = "select g.* from gupiao g" +
            " inner join v_syn_max_bizdate v on 1 = 1" +
            " LEFT JOIN gupiao_kline k on k.symbol = g.symbol and v.biz_date = k.biz_date" +
            " where (g.symbol like '11%' or  g.symbol like '12%') and k.symbol  is null LIMIT 0, 20", nativeQuery = true)
    List<Gupiao> listkzz1Day();

    @Query(value = "select g.* from gupiao g" +
            " inner join v_syn_max_bizdate v on 1 = 1" +
            " LEFT JOIN gupiao_kline_30m k on k.symbol = g.symbol and CONCAT(v.biz_date,' 15:00')  =  k.biz_date " +
            " where (g.symbol like '11%' or  g.symbol like '12%') and k.symbol  is null LIMIT 0, 20", nativeQuery = true)
    List<Gupiao> listkzz30M();

    @Query(value = "select g.* from gupiao g" +
            " inner join v_syn_max_bizdate v on 1 = 1" +
            " LEFT JOIN gupiao_kline_5m k on k.symbol = g.symbol and CONCAT(v.biz_date,' 15:00')  =  k.biz_date " +
            " where (g.symbol like '11%' or  g.symbol like '12%') and k.symbol  is null LIMIT 0, 20", nativeQuery = true)
    List<Gupiao> listkzz5M();




    @Query(value = "select s.* from gupiao s " +
            " inner join v_before_30m_time b on 1= 1" +
            " left join gupiao_kline_30m k on s.symbol = k.symbol and b.biz_date = k.biz_date" +
            " where k.symbol is null order by s.total_shares LIMIT 0, 20", nativeQuery = true)
    List<Gupiao> listBeforeTime30m(Integer period);



    @Query(value = "select s.* from gupiao s " +
            " inner join v_before_30m_time b on 1= 1" +
            " inner join gupiao_kline_30m k on s.symbol = k.symbol and b.biz_date = k.biz_date" +
            " where k.symbol = ?1", nativeQuery = true)
    Gupiao getBeforeTime30m(String symbol, Integer period);


    @Query(value = "select s.* from gupiao s " +
            " inner join v_before_5m_time b on 1= 1" +
            " left join gupiao_kline_5m k on s.symbol = k.symbol and b.biz_date = k.biz_date" +
            " where k.symbol is null order by s.total_shares LIMIT 0, 20", nativeQuery = true)
    List<Gupiao> listBeforeTime5m(Integer period);


    @Query(value = "select s.* from gupiao s " +
            " inner join v_before_5m_time b on 1= 1" +
            " inner join gupiao_kline_5m k on s.symbol = k.symbol and b.biz_date = k.biz_date" +
            " where k.symbol = ?1", nativeQuery = true)
    Gupiao getBeforeTime5m(String symbol, Integer period);


    @Modifying
    @Transactional
    @Query(value = "delete from  gupiao  where symbol like '11%' or  symbol like '12%'  ",nativeQuery = true)
    int delKzzAll();

    @Modifying
    @Transactional
    @Query(value = "delete from gupiao where (symbol like '11%' or  symbol like '12%') and symbol not in (select * from ( select symbol from gupiao where symbol like '11%' or  symbol like '12%' order by amount desc  LIMIT 0, 100 ) t)  ",nativeQuery = true)
    int delNotUse();



    @Query(value = "select MIN(biz_date) as biz_date from(" +
            "select * from biz_calendar where holiday = 1 and biz_date < DATE_FORMAT(NOW(),'%Y-%m-%d') ORDER BY biz_date desc LIMIT 0, ?1 ) t ", nativeQuery = true)
    String getBizDate(int num);
}
