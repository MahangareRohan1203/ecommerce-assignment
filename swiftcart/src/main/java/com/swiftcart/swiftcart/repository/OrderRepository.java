package com.swiftcart.swiftcart.repository;

import com.swiftcart.swiftcart.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    public List<Orders> findByCustomer_CustomerId(long id);
}
