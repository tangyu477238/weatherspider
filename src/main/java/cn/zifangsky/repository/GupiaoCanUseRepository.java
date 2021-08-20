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

    @Query(value = " select distinct symbol from gupiao_can_use ", nativeQuery = true)
    List<GupiaoCanUse> listSyns();

    @Modifying
    @Transactional
    @Query(value = "delete from  gupiao  where symbol like '11%' or  symbol like '12%'  ",nativeQuery = true)
    int delKzzAll();


}
