package com.swiftcart.swiftcart.repository;

import com.swiftcart.swiftcart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    public CartItem findByCart_cartIdAndProduct_productIdAndColorAndSize(long cartId, long productId, String color, String size);
}