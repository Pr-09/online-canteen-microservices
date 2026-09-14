package com.OnlineCanteen.AdminService.Repository;



import com.OnlineCanteen.AdminService.Entity.UdharCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UdharCustomerRepository extends JpaRepository<UdharCustomer, Long> {
    Optional<UdharCustomer> findByEmail(String email);

    Optional<UdharCustomer> findByUserId(Long userId);

//    UdharCustomer findByUserId(long userId);
}