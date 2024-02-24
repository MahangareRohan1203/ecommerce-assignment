package com.swiftcart.swiftcart.serviceImplementation;

import com.swiftcart.swiftcart.entity.Category;
import com.swiftcart.swiftcart.entity.Customer;
import com.swiftcart.swiftcart.entity.Product;
import com.swiftcart.swiftcart.exception.CustomerException;
import com.swiftcart.swiftcart.repository.CategoryRepository;
import com.swiftcart.swiftcart.repository.CustomerRepository;
import com.swiftcart.swiftcart.repository.ProductRepository;
import com.swiftcart.swiftcart.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImplementation implements CustomerService {


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Customer addNewCustomer(Customer customer) throws CustomerException {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent())
            throw new CustomerException("Email Already Exists");

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer = customerRepository.save(customer);
        return customer;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with given id"));
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }
}

