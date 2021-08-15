package cn.zifangsky.repository;

import cn.zifangsky.model.GupiaoKline5m;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface GupiaoKline5mRepository extends JpaRepository<GupiaoKline5m,Integer> {



}
