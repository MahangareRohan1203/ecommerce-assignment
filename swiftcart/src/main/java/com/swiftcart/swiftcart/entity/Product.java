package com.swiftcart.swiftcart.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String title;
    private String description;
    private String image;
    private double price;

    @OneToMany(mappedBy = "product")
    private List<Availability> availabilityList = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}
