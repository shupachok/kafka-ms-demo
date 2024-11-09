package com.sp.core.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFailedEvent {
    UUID orderId;
    UUID ticketId;
    Integer ticketQuantity;
}
