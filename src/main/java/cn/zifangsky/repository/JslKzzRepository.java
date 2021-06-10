package cn.zifangsky.repository;

import cn.zifangsky.model.JslKzz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JslKzzRepository extends JpaRepository<JslKzz,Integer> {

    JslKzz findByBondIdAndSscDt(String bondId, String sscDt);

    JslKzz findByBondId(String bondId);
}
