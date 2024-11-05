package com.sp.core.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentProcessedEvent {
    UUID orderId;
    UUID paymentId;
}
