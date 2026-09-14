package com.OnlineCanteen.OrderService.entity;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class UdharCode_NO {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private boolean used;
}
