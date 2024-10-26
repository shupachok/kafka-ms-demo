package com.sp.core.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TicketInsufficientQuantityException extends RuntimeException {
    private final UUID productId;
    private final UUID orderId;

    public TicketInsufficientQuantityException(UUID productId, UUID orderId) {
        super("Product " + productId + " has insufficient quantity in the stock for order " + orderId);
        this.productId = productId;
        this.orderId = orderId;
    }
}
