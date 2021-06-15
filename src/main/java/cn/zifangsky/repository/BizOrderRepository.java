package cn.zifangsky.repository;

import cn.zifangsky.model.BizOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BizOrderRepository extends JpaRepository<BizOrder,Integer> {
    BizOrder findByOrderNo(String orderNo);
}
