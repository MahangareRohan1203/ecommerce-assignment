package com.swiftcart.swiftcart.repository;

import com.swiftcart.swiftcart.entity.Customer;
import com.swiftcart.swiftcart.exception.CustomerException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Optional<Customer> findByEmail(String email) throws UsernameNotFoundException;
}
