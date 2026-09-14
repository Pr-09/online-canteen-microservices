package com.OnlineCanteen.OrderService.repository;

import com.OnlineCanteen.OrderService.entity.UdharCode_NO;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UdharCodeRepository extends JpaRepository<UdharCode_NO, Long> {
    Optional<UdharCode_NO> findByCode(String code);
}
