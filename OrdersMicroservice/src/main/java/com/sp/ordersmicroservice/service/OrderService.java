package com.sp.ordersmicroservice.service;


import com.sp.core.dto.Order;

public interface OrderService {
    Order placeOrder(Order order);
}
