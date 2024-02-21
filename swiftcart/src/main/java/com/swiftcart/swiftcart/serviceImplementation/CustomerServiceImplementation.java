package com.swiftcart.swiftcart.serviceImplementation;

import com.swiftcart.swiftcart.entity.Customer;
import com.swiftcart.swiftcart.exception.CustomerException;
import com.swiftcart.swiftcart.repository.CustomerRepository;
import com.swiftcart.swiftcart.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImplementation implements CustomerService {


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer addNewCustomer(Customer customer) throws CustomerException {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent())
            throw new CustomerException("Email Already Exists");

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer = customerRepository.save(customer);
        return customer;
    }
}
