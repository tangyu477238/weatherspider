package cn.zifangsky.repository;

import cn.zifangsky.model.XueqiuGupiao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XueqiuGupiaoRepository extends JpaRepository<XueqiuGupiao,Integer> {

    XueqiuGupiao findBySymbol(String bondId);
}
