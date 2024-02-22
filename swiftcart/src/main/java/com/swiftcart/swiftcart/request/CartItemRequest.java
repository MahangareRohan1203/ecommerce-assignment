package com.swiftcart.swiftcart.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequest {

    @NotNull
    private Long productId;


    private PRODUCT_SIZE size;
    private String color;
    private int quantity;

    public static enum PRODUCT_SIZE{
        S, M, L, XL, XXL
    }
}

