package com.food.order.system.payment.data.access.creditentry.repository;

import com.food.order.system.payment.data.access.creditentry.entity.CreditEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditEntryJpaRepository extends JpaRepository<CreditEntryEntity, UUID> {

    Optional<CreditEntryEntity> findByCustomerId(UUID customerId);


}
