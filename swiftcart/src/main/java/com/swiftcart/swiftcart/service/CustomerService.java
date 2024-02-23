package com.swiftcart.swiftcart.service;

import com.swiftcart.swiftcart.entity.Customer;
import com.swiftcart.swiftcart.exception.CustomerException;

public interface CustomerService {

    public Customer addNewCustomer(Customer customer) throws CustomerException;


}
