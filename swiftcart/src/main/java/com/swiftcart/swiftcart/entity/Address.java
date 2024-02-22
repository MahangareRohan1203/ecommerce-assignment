package com.swiftcart.swiftcart.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Length(min = 6, message = "First Line should contains at least 6 characters")
    private String firstLine;

    @Length(min = 6, message = "Second Line should contains at least 6 characters")
    private String secondLine;

    @Length(min = 2, message = "Second Line should contains at least 2 characters")
    private String city;

    @Length(min = 2, message = "Second Line should contains at least 2 characters")
    private String state;

    @Length(max = 6, min = 6, message = "PinCode should be 6 digit")
    private String pinCode;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;
}
