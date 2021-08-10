package cn.zifangsky.repository;

import cn.zifangsky.model.Gupiao;
import cn.zifangsky.model.GupiaoKline5m;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GupiaoRepository extends JpaRepository<Gupiao,Integer> {

    Gupiao findBySymbol(String bondId);

    @Query(value = "select * from gupiao where symbol like '11%' or  symbol like '12%' ", nativeQuery = true)
    List<Gupiao> getSymbolTop();

}
