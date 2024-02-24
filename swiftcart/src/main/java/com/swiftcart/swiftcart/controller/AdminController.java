package com.swiftcart.swiftcart.controller;

import com.swiftcart.swiftcart.config.JwtGenerator;
import com.swiftcart.swiftcart.entity.Availability;
import com.swiftcart.swiftcart.entity.Customer;
import com.swiftcart.swiftcart.entity.Product;
import com.swiftcart.swiftcart.exception.CustomerException;
import com.swiftcart.swiftcart.reponse.LogInResponse;
import com.swiftcart.swiftcart.request.ProductRequest;
import com.swiftcart.swiftcart.service.AdminService;
import com.swiftcart.swiftcart.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
public class AdminController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private AdminService adminService;

    @PostMapping("/admin/products") // ✅
    public ResponseEntity<Product> addNewProductHandler(@RequestBody @Valid ProductRequest product) {
        Product saved = adminService.addNewProductHandler(product);
        return ResponseEntity.accepted().body(saved);
    }

    @PostMapping("/admin") // ✅
    public ResponseEntity<Customer> addNewAdmin(@RequestBody Customer customer) throws CustomerException {
        customer.setRole("ADMIN");
        customer = customerService.addNewCustomer(customer);
        return ResponseEntity.accepted().body(customer);
    }

    @GetMapping("/admin/signIn") // ✅
    public ResponseEntity<LogInResponse> adminLogInHandler(Authentication authentication) {
        LogInResponse response = new LogInResponse(jwtGenerator.generateToken(), "Log in successful", LocalDateTime.now());
        return ResponseEntity.accepted().body(response);
    }

    @PostMapping("/admin/products/availability") // ✅
    public ResponseEntity<Availability> addNewAvailabilityHandler(@RequestParam long productId, @Valid @RequestBody Availability availability) {
        availability = adminService.addNewAvailability(productId, availability);
        return ResponseEntity.accepted().body(availability);
    }

}
