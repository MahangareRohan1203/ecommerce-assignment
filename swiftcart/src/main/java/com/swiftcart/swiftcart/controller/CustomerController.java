package com.swiftcart.swiftcart.controller;

import com.swiftcart.swiftcart.config.JwtGenerator;
import com.swiftcart.swiftcart.entity.Customer;
import com.swiftcart.swiftcart.exception.CustomerException;
import com.swiftcart.swiftcart.reponse.LogInResponse;
import com.swiftcart.swiftcart.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping("")
    public ResponseEntity<Customer> addNewCustomerHandler(@RequestBody Customer customer) throws CustomerException {
        customer.setRole("USER");
        customer = customerService.addNewCustomer(customer);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @GetMapping("/signIn")
    public ResponseEntity<LogInResponse> signInHandler(Authentication authentication) {
        return new ResponseEntity<>(new LogInResponse(jwtGenerator.generateToken(), "Log In Successful ", LocalDateTime.now()), HttpStatus.OK);

    }
}
