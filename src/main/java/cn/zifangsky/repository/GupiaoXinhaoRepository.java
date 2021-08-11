package cn.zifangsky.repository;


import cn.zifangsky.model.GupiaoXinhao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GupiaoXinhaoRepository extends JpaRepository<GupiaoXinhao,Integer> {

    GupiaoXinhao findBySymbolAndTypeNameAndBizDate(String bondId, String typeName, String bizDate);
}
