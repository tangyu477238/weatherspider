package cn.zifangsky.repository;

import cn.zifangsky.model.Gupiao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GupiaoRepository extends JpaRepository<Gupiao,Integer> {

    Gupiao findBySymbol(String bondId);

}
