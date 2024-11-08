package com.sp.ordersmicroservice.service;


import com.sp.core.dto.Order;

import java.util.UUID;

public interface OrderService {
    Order placeOrder(Order order);

    void approveOrder(UUID orderId);
}
