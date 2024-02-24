package com.swiftcart.swiftcart.controller;

import com.swiftcart.swiftcart.config.JwtGenerator;
import com.swiftcart.swiftcart.entity.*;
import com.swiftcart.swiftcart.exception.CartException;
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


    @GetMapping("/products")  // ✅
    public ResponseEntity<List<Product>> getAllProductsHandler() {
        List<Product> list = customerService.getAllProducts();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/products/{id}") // ✅
    public ResponseEntity<Product> getProductByIdHandler(@PathVariable Long id) {
        Product product = customerService.findProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/categories") // ✅
    public ResponseEntity<List<Category>> getAllCategoriesHandler() {
        List<Category> list = customerService.findAllCategories();
        return ResponseEntity.ok(list);
    }


    @PostMapping("/users") // ✅
    public ResponseEntity<Customer> addNewCustomerHandler(@RequestBody Customer customer) throws CustomerException {
        customer.setRole("USER");
        customer = customerService.addNewCustomer(customer);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @GetMapping("/users/signIn") // ✅
    public ResponseEntity<LogInResponse> signInHandler(Authentication authentication) {
        return new ResponseEntity<>(new LogInResponse(jwtGenerator.generateToken(), "Log In Successful ", LocalDateTime.now()), HttpStatus.OK);

    }

    @GetMapping("/cart") // ✅
    public ResponseEntity<Cart> getAllCartItemsHandler(@RequestHeader("Authorization") String jwt) {
        String email = jwtGenerator.getEmailFromToken(jwt);
        Cart cart = customerService.getCart(email);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/cart") // ✅
    public ResponseEntity<CartItem> createCartItem(@RequestHeader("Authorization") String jwt, @RequestBody @Valid CartItemRequest item) throws CartException {
        String email = jwtGenerator.getEmailFromToken(jwt);
        CartItem added = customerService.addProductToCart(email, item);
        return new ResponseEntity<>(added, HttpStatus.OK);
    }

    @PatchMapping("/cart/{id}") // ✅
    public ResponseEntity<CartItem> updateCartItem(@RequestHeader("Authorization") String jwt, @RequestBody CartItemRequest cartItem, @PathVariable Long id) throws CartException {
        String email = jwtGenerator.getEmailFromToken(jwt);
        CartItem item = customerService.updateCartItem(email, cartItem, id);
        return ResponseEntity.accepted().body(item);
    }

    @DeleteMapping("/cart/{id}") // ✅
    public ResponseEntity<Response> deleteCartItem(@RequestHeader("Authorization") String jwt, @PathVariable long id) throws CartException {
        String email = jwtGenerator.getEmailFromToken(jwt);
        String message = customerService.deleteCartItem(email, id);
        return ResponseEntity.accepted().body(new Response(message));
    }

    @PostMapping("/orders") //
    public ResponseEntity<Orders> createNewOrderHandler(@RequestHeader("Authorization") String jwt, @RequestBody Address address) throws CustomerException, CartException {
        String email = jwtGenerator.getEmailFromToken(jwt);
        Orders order = customerService.createNewOrder(email, address);
        return ResponseEntity.accepted().body(order);
    }

    @GetMapping("/orders") //
    public ResponseEntity<List<Orders>> getAllOrdersOfUsersHandler(@RequestHeader("Authorization") String jwt) throws CustomerException {
        String email = jwtGenerator.getEmailFromToken(jwt);
        List<Orders> list = customerService.findAllOrders(email);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/orders/{id}") //
    public ResponseEntity<Orders> getOrderDetailsHandler(@RequestHeader("Authorization") String jwt, @PathVariable long id) throws CustomerException {
        String email = jwtGenerator.getEmailFromToken(jwt);
        Orders order = customerService.findOrderById(email, id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders/{id}/payment") //
    public ResponseEntity<Null> createPaymentLinkHandler(@RequestHeader("Authorization") String jwt, @PathVariable long id) {
        return ResponseEntity.ok(null);
    }


}
