package com.OnlineCanteen.OrderService.repository;

import com.OnlineCanteen.OrderService.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
