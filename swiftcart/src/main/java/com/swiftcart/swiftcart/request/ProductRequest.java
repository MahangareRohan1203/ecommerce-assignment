package com.swiftcart.swiftcart.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class ProductRequest {

    @NotNull
    private String title;

    @Min(value = 1)
    private double price;

    @NotNull
    private String description;

    @NotNull
    private String image;

    @NotNull
    private String topLevelCategory;

    @NotNull
    private String secondLevelCategory;

    @NotNull
    private String thirdLevelCategory;
}
