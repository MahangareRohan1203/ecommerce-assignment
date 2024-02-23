package com.swiftcart.swiftcart.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private Category parentCategory;

    private Integer level;
}
