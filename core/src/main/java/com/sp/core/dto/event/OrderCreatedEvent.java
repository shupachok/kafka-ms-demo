package com.sp.core.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderCreatedEvent {
    private UUID orderId;
    private UUID customerId;
    private UUID ticketId;
    private Integer ticketQuantity;
}
