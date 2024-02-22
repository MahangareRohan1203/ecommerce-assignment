package com.swiftcart.swiftcart.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private Category parentCategory;
}
