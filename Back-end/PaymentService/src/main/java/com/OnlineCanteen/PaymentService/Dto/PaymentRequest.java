package com.OnlineCanteen.PaymentService.Dto;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long orderId;

    private Long userId;

    private Double amount;

    private String paymentMethod;
}
