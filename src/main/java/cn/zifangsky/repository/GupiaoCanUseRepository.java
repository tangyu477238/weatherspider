package cn.zifangsky.repository;

import cn.zifangsky.model.GupiaoCanUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface GupiaoCanUseRepository extends JpaRepository<GupiaoCanUse,Integer> {

    @Query(value = " select v.* from v_xinhao_track v " +
            " left join gupiao_can_use c on c.symbol = v.symbol and c.period = v.period and c.biz_date = v.biz_date and c.stype =  v.stype" +
            " where c.symbol is null", nativeQuery = true)
    List<Map<String, Object>> listCanUseView();

    @Query(value = " select distinct c.symbol from gupiao_can_use c " +
            "inner join (\n" +
            "    select  MIN(biz_date) as biz_date  from (\n" +
            "        select c.biz_date from v_syn_max_bizdate b\n" +
            "        inner join biz_calendar c on c.biz_date <= b.biz_date and c.holiday = 1\n" +
            "        order by biz_date desc  LIMIT 0, 3 ) t\n" +
            ") b on  c.biz_date >= b.biz_date ", nativeQuery = true)
    List<String> listSyns();

    @Modifying
    @Transactional
    @Query(value = "delete from  gupiao  where symbol like '11%' or  symbol like '12%'  ",nativeQuery = true)
    int delKzzAll();

    @Query(value = " select * from v_buylist_5m ", nativeQuery = true)
    List<Map<String, Object>> listBuy();

    @Query(value = " select * from v_buylist_ma ", nativeQuery = true)
    List<Map<String, Object>> listBuyMa();

    @Query(value = " select * from v_buylist_last_morn where account=?1", nativeQuery = true)
    List<Map<String, Object>> listBuyLastMorn(String account);

    @Query(value = "select token from biz_buy_amount t where account = ?1 ", nativeQuery = true)
    String getToken(String account);

    @Query(value = "select stime from biz_buy_amount t where account = ?1 ", nativeQuery = true)
    String getStartTime(String account);

    @Query(value = "select etime from biz_buy_amount t where account = ?1 ", nativeQuery = true)
    String getEndTime(String account);

}
