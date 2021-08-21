package cn.zifangsky.repository;

import cn.zifangsky.model.Gupiao;
import cn.zifangsky.model.GupiaoKline5m;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GupiaoRepository extends JpaRepository<Gupiao,Integer> {

    Gupiao findBySymbol(String bondId);

    @Query(value = "select * from gupiao where symbol like '11%' or  symbol like '12%' ", nativeQuery = true)
    List<Gupiao> getSymbolTop();


    @Modifying
    @Transactional
    @Query(value = "delete from  gupiao  where symbol like '11%' or  symbol like '12%'  ",nativeQuery = true)
    int delKzzAll();


    @Query(value = "select MIN(biz_date) as biz_date from(" +
            "select * from biz_calendar where holiday = 1 and biz_date < DATE_FORMAT(NOW(),'%Y-%m-%d') ORDER BY biz_date desc LIMIT 0, ?1 ) t ", nativeQuery = true)
    String getBizDate(int num);
}
