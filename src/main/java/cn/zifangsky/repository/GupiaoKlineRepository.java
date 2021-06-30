package cn.zifangsky.repository;

import cn.zifangsky.model.GupiaoKline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface GupiaoKlineRepository extends JpaRepository<GupiaoKline,Integer> {

    GupiaoKline findBySymbol(String bondId);

    GupiaoKline findBySymbolAndPeriodAndTimestamp(String bondId, String period, Date date);

    GupiaoKline findBySymbolAndPeriodAndBizDate(String bondId, String period, String bizDate);



}
