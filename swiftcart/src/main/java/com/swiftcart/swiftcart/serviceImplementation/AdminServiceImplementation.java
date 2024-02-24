package com.swiftcart.swiftcart.serviceImplementation;

import com.swiftcart.swiftcart.entity.Availability;
import com.swiftcart.swiftcart.entity.Category;
import com.swiftcart.swiftcart.entity.Product;
import com.swiftcart.swiftcart.repository.AvailabilityRepository;
import com.swiftcart.swiftcart.repository.CategoryRepository;
import com.swiftcart.swiftcart.repository.ProductRepository;
import com.swiftcart.swiftcart.request.ProductRequest;
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

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Override
    public Product addNewProductHandler(ProductRequest productRequest) {
        Category topLevel = categoryRepository.findByName(productRequest.getTopLevelCategory());

        if (topLevel == null) {
            Category top = new Category();
            top.setName(productRequest.getTopLevelCategory());
            top.setLevel(1);
            topLevel = saveCategory(top);
        }

        Category secondLevel = categoryRepository.findByNameAndParentCategory_Name(productRequest.getSecondLevelCategory(), productRequest.getTopLevelCategory());
        if (secondLevel == null) {
            Category second = new Category();
            second.setName(productRequest.getSecondLevelCategory());
            second.setLevel(2);
            second.setParentCategory(topLevel);
            secondLevel = saveCategory(second);
        }

        Category thirdLevel = categoryRepository.findByNameAndParentCategory_Name(productRequest.getThirdLevelCategory(), productRequest.getSecondLevelCategory());
        if (thirdLevel == null) {
            Category third = new Category();
            third.setName(productRequest.getThirdLevelCategory());
            third.setLevel(3);
            third.setParentCategory(secondLevel);
            thirdLevel = saveCategory(third);
        }
        Product product = new Product();
        product.setTitle(productRequest.getTitle());
        product.setPrice(productRequest.getPrice());
        product.setImage(productRequest.getImage());
        product.setDescription(productRequest.getDescription());
        product.setCategory(thirdLevel);
        productRepository.save(product);
        return product;
    }

    @Override
    public Availability addNewAvailability(long productId, Availability availability) {
        Product product = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product Not found with given id"));

        Availability existed = availabilityRepository.findByProduct_productIdAndColorAndSize(productId, availability.getColor(), availability.getSize());
        if(existed == null){
            availability.setProduct(product);
            existed = availabilityRepository.save(availability);
        }else{
            existed.setQuantity(existed.getQuantity() + availability.getQuantity());
            existed.setPriceOfEach(availability.getPriceOfEach());
            availabilityRepository.save(existed);
        }
        return existed;
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
}
