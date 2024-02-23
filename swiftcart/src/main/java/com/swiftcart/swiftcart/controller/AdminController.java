package com.swiftcart.swiftcart.controller;

import com.swiftcart.swiftcart.config.JwtGenerator;
import com.swiftcart.swiftcart.entity.Category;
import com.swiftcart.swiftcart.entity.Customer;
import com.swiftcart.swiftcart.entity.Product;
import com.swiftcart.swiftcart.exception.CustomerException;
import com.swiftcart.swiftcart.reponse.LogInResponse;
import com.swiftcart.swiftcart.service.AdminService;
import com.swiftcart.swiftcart.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private AdminService adminService;

    @PostMapping("/admin/products")
    public ResponseEntity<Product> addNewProductHandler(@RequestBody Product product) {
        product = adminService.addNewProductHandler(product);
        return ResponseEntity.accepted().body(product);
    }

    @PostMapping("/admin")
    public ResponseEntity<Customer> addNewAdmin(@RequestBody Customer customer) throws CustomerException {
        customer.setRole("ADMIN");
        customer = customerService.addNewCustomer(customer);
        return ResponseEntity.accepted().body(customer);
    }

    @GetMapping("/admin/signIn")
    public ResponseEntity<LogInResponse> adminLogInHandler(Authentication authentication) {
        LogInResponse response = new LogInResponse(jwtGenerator.generateToken(), "Log in successful", LocalDateTime.now());
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProductsHandler() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductByIdHandler(@PathVariable Long id) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategoriesHandler() {
        return ResponseEntity.ok(null);
    }


}
