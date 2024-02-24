package com.swiftcart.swiftcart.service;

import com.swiftcart.swiftcart.entity.*;
import com.swiftcart.swiftcart.exception.CartException;
import com.swiftcart.swiftcart.exception.CustomerException;
import com.swiftcart.swiftcart.request.CartItemRequest;

import java.util.List;

public interface CustomerService {

    public Customer addNewCustomer(Customer customer) throws CustomerException;

    public List<Product> getAllProducts();

    public Product findProductById(long id);

    public List<Category> findAllCategories();

    public Cart getCart(String email);

    public CartItem addProductToCart(String email, CartItemRequest cartItem) throws CartException;

    public CartItem updateCartItem(String email, CartItemRequest cartItemRequest, long cartItemId) throws CartException;

    public String deleteCartItem(String email, long id) throws CartException;

    public Orders createNewOrder(String email, Address address) throws CartException, CustomerException;

    List<Orders> findAllOrders(String email) throws CustomerException;

    Orders findOrderById(String email, long id)  throws CustomerException;
}
