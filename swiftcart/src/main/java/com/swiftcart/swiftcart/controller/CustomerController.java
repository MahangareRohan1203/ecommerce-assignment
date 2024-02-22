package com.swiftcart.swiftcart.controller;

import com.swiftcart.swiftcart.config.JwtGenerator;
import com.swiftcart.swiftcart.entity.Cart;
import com.swiftcart.swiftcart.entity.CartItem;
import com.swiftcart.swiftcart.entity.Customer;
import com.swiftcart.swiftcart.entity.Orders;
import com.swiftcart.swiftcart.exception.CustomerException;
import com.swiftcart.swiftcart.reponse.LogInResponse;
import com.swiftcart.swiftcart.reponse.Response;
import com.swiftcart.swiftcart.request.CartItemRequest;
import com.swiftcart.swiftcart.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping("/users")
    public ResponseEntity<Customer> addNewCustomerHandler(@RequestBody Customer customer) throws CustomerException {
        customer.setRole("USER");
        customer = customerService.addNewCustomer(customer);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @GetMapping("/users/signIn")
    public ResponseEntity<LogInResponse> signInHandler(Authentication authentication) {
        return new ResponseEntity<>(new LogInResponse(jwtGenerator.generateToken(), "Log In Successful ", LocalDateTime.now()), HttpStatus.OK);

    }

    @GetMapping("/cart")
    public ResponseEntity<List<CartItem>> getAllCartItemsHandler(@RequestHeader("Authorization") String jwt) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/cart")
    public ResponseEntity<CartItem> createCartItem(@RequestHeader("Authorization") String jwt, @RequestBody @Valid CartItemRequest item) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PatchMapping("/cart/{id}")
    public ResponseEntity<CartItem> updateCartItem(@RequestHeader("Authorization") String jwt, @RequestBody CartItem cartItem, @PathVariable Long id){
        return ResponseEntity.accepted().body(null);
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<Response> deleteCartItem(@RequestHeader("Authorization") String jwt, @PathVariable long id){
        return ResponseEntity.accepted().body(null);
    }

    @PostMapping("/orders")
    public ResponseEntity<Orders> createNewOrderHandler(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.accepted().body(null);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Orders>> getAllOrdersOfUsersHandler(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok(null);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Orders> getOrderDetailsHandler(@RequestHeader("Authorization") String jwt, @PathVariable long id){
        return ResponseEntity.ok(null);
    }

    @GetMapping("/orders/{id}/payment")
    public  ResponseEntity<Null> createPaymentLinkHandler(@RequestHeader("Authorization") String jwt, @PathVariable long id){
        return ResponseEntity.ok(null);
    }



}
