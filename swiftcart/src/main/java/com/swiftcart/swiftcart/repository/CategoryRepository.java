package com.swiftcart.swiftcart.repository;

import com.swiftcart.swiftcart.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    public Optional<Category> findByName(String name);

    public Category findByNameAndParentCategory_Name(String name, String parent);
}
