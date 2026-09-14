package com.OnlineCanteen.AdminService.Repository;

import com.OnlineCanteen.AdminService.Entity.UdharCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface UdharCodeRepository extends JpaRepository<UdharCode, Long> {
    Optional<UdharCode> findByCode(String code);
}