package com.OnlineCanteen.OrderService.dto;

import lombok.Data;

@Data
public class MenuItemResponse {
    private Long id;

    private String name;

    private double price;

    private int prepTime;

    private boolean readyMade;
}
