package com.swiftcart.swiftcart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String size;

    @NotNull
    private String color;

    @Min(value = 1)
    private long quantity;

    @Min(value = 1)
    private double priceOfEach;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "productId")
    @JsonIgnore
    private Product product;
}
