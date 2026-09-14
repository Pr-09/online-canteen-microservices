package com.OnlineCanteen.AdminService.Repository;

import com.OnlineCanteen.AdminService.Entity.UdharTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UdharTransactionRepository extends JpaRepository<UdharTransaction, Long> {
    List<UdharTransaction> findByUserId(Long customerId);
}
