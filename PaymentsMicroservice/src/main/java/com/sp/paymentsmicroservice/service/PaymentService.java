package com.sp.paymentsmicroservice.service;

import com.sp.core.dto.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment process(Payment payment);
}