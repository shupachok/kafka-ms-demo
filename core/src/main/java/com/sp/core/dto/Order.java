package com.sp.core.dto;


import com.sp.core.type.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    private UUID orderId;
    private UUID customerId;
    private UUID ticketId;
    private Integer ticketQuantity;
    private OrderStatus status;
}
