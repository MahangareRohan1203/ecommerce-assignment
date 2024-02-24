package com.swiftcart.swiftcart.service;

import com.swiftcart.swiftcart.entity.Category;
import com.swiftcart.swiftcart.entity.Customer;
import com.swiftcart.swiftcart.entity.Product;
import com.swiftcart.swiftcart.exception.CustomerException;

import java.util.List;

public interface CustomerService {

    public Customer addNewCustomer(Customer customer) throws CustomerException;

    public List<Product> getAllProducts();

    public Product findProductById(long id);

    public List<Category> findAllCategories();
}
