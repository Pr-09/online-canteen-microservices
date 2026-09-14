package com.OnlineCanteen.OrderService.dto;


import lombok.Data;

@Data
public class MenuItem {
    private Long id;
    private String name;
    private double price;
    private int prepTime;
}