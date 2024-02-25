package com.swiftcart.swiftcart.entity;

import com.swiftcart.swiftcart.enums.OrderStatus;
import com.swiftcart.swiftcart.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Payment {

    @Id
    private String paymentID;

    private String razorpayPaymentId;

    private double amount;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Orders order;
}
