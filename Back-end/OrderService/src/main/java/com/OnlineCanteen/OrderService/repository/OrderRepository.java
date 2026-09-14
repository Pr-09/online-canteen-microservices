package com.OnlineCanteen.OrderService.repository;


import com.OnlineCanteen.OrderService.dto.OrderRequest;
import com.OnlineCanteen.OrderService.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserId(Long userId);
    List<Orders> findByStatus(String status);
}
