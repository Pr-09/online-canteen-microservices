package com.OnlineCanteen.OrderService.dto;

import lombok.Data;
import java.util.List;
@Data
public class OrderRequest   {
    private int tableNumber;
    private long id;
    private Double totalAmount;
    private String status;
    private String paymentType;
    private String udharCode;

    private String customerName;
    private String email;

    private List<OrderItemRequest> items;
}
