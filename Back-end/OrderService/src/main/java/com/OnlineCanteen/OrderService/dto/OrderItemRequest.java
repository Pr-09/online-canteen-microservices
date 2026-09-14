package com.OnlineCanteen.OrderService.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long menuId;
    private int quantity;
    private int price;

}

