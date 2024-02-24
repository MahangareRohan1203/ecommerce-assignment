package com.swiftcart.swiftcart.service;


import com.swiftcart.swiftcart.entity.Availability;
import com.swiftcart.swiftcart.entity.Product;
import com.swiftcart.swiftcart.request.ProductRequest;

public interface AdminService {

    public Product addNewProductHandler(ProductRequest product);

    public Availability addNewAvailability(long productId, Availability availability);

}
