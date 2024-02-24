package com.swiftcart.swiftcart.repository;

import com.swiftcart.swiftcart.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    public Availability findByProduct_productIdAndColorAndSize(long id, String color, String size);

}
