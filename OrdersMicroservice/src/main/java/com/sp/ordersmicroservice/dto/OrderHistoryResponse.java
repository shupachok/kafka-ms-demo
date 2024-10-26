package com.sp.ordersmicroservice.dto;

import com.sp.core.type.OrderStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class OrderHistoryResponse {
    private UUID id;
    private UUID orderId;
    private OrderStatus status;
    private Timestamp createdAt;
}
