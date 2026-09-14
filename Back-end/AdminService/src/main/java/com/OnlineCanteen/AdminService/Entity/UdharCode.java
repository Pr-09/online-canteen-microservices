package com.OnlineCanteen.AdminService.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
@Table(name = "udhar_codes")
public class UdharCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(unique = true)
    private String code;

    private Long customerId;

    private boolean used;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    /*
     future migration:
     replace OTP with QR approval session
   */
    private String approvalSessionId;
}