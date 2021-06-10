package cn.zifangsky.repository;

import cn.zifangsky.model.XueqiuGupiaoGainian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XueqiuGupiaoGainianRepository extends JpaRepository<XueqiuGupiaoGainian,Integer> {

    XueqiuGupiaoGainian findBySymbolAndGainian(String bondId,String gainian);
}
