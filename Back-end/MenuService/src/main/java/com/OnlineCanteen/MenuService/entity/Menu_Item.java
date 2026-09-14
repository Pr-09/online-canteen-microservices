package com.OnlineCanteen.MenuService.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "menu_items")
public class Menu_Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private int prepTime;
    private String description;

    private String category;

    private boolean available;
    private boolean readyMade;

    private String imageUrl;
}
