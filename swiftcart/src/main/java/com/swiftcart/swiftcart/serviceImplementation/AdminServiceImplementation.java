package com.swiftcart.swiftcart.serviceImplementation;

import com.swiftcart.swiftcart.entity.Category;
import com.swiftcart.swiftcart.entity.Product;
import com.swiftcart.swiftcart.repository.CategoryRepository;
import com.swiftcart.swiftcart.repository.ProductRepository;
import com.swiftcart.swiftcart.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImplementation implements AdminService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product addNewProductHandler(Product product) {
        if (product.getCategory() != null && product.getCategory().getParentCategory() != null) {
            Category category = categoryRepository.findByNameAndParentCategory_Name(product.getCategory().getName(), product.getCategory().getParentCategory().getName());

            if(category == null){

            }

            Category parent = saveCategory(product.getCategory().getParentCategory());
            product.getCategory().setParentCategory(parent);
            product.setCategory(saveCategory(product.getCategory()));
        } else if (product.getCategory() != null) {
            System.out.println("CALLED THIS - 2");
            product.setCategory(saveCategory(product.getCategory()));
        } else {
            System.out.println("CATEGORY IS NULL");
        }
        product = productRepository.save(product);
        return product;
    }

    public Category saveCategory(Category category) {
        Optional<Category> parent = categoryRepository.findByName(category.getName());
        if (parent.isPresent()) {
            return parent.get();
        } else {
            return categoryRepository.save(category);
        }
    }
}
