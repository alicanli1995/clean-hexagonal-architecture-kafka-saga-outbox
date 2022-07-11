package com.food.order.system.data.access.customer.repository;

import com.food.order.system.data.access.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerJPARepository extends JpaRepository<CustomerEntity, UUID> {
}
