package com.swiftcart.swiftcart.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;
    private String color;
    private String quantity;
    private double priceOfEach;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
