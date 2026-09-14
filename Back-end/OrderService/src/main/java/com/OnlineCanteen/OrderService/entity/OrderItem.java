package com.OnlineCanteen.OrderService.entity;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuId;
    private int quantity;
    private double price;

//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Orders order;
}
