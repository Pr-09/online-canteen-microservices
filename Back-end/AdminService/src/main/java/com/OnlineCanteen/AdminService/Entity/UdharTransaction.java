package com.OnlineCanteen.AdminService.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "udhar_transactions")
public class UdharTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long orderId;
    private Double amount;

    private String status; // PENDING / PAID

    private LocalDateTime createdAt;
}