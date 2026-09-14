package com.OnlineCanteen.AdminService.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "udhar_customers")
public class UdharCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
    linked to authenticated auth user
    helps prevent misuse
  */
    private Long userId;

    private String name;
    private String email;
    private String mobile;
    private String studentId;

    private Double creditLimit = 500.0;
    private Double pendingAmount = 0.0;

    private boolean approved = true;



}