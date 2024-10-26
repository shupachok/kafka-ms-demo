package com.sp.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private UUID id;
    private UUID orderId;
    private UUID ticketId;
    private BigDecimal ticketPrice;
    private Integer ticketQuantity;

    public Payment(UUID orderId, UUID ticketId, BigDecimal ticketPrice, Integer ticketQuantity) {
        this.orderId = orderId;
        this.ticketId = ticketId;
        this.ticketPrice = ticketPrice;
        this.ticketQuantity = ticketQuantity;
    }
}

