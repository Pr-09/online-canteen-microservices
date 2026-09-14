package com.OnlineCanteen.PaymentService.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long userId;

    private Double amount;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String signature;

    /*
      CREATED
      SUCCESS
      FAILED
      ORDER_UPDATE_PENDING
    */

    private String status;

    private String paymentMethod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
