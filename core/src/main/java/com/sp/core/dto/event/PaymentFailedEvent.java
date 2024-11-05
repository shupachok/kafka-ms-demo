package com.sp.core.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentFailedEvent {
    UUID orderId;
    UUID ticketId;
    Integer ticketQuantity;
}
