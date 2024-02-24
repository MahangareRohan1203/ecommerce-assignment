package com.swiftcart.swiftcart.entity;

import com.swiftcart.swiftcart.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private double totalPrice;

    private Date orderDate ;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private  Date expectedDeliveryDate;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "addressId")
    private Address address;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderList = new ArrayList<>();

}
