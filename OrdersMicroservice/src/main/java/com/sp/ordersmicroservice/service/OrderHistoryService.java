package com.sp.ordersmicroservice.service;


import com.sp.core.type.OrderStatus;
import com.sp.ordersmicroservice.dto.OrderHistory;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryService {
    void add(UUID orderId, OrderStatus orderStatus);

    List<OrderHistory> findByOrderId(UUID orderId);
}
