package com.sp.paymentsmicroservice.dao.jpa.repository;

import com.sp.paymentsmicroservice.dao.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
}