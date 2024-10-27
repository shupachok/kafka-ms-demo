package com.sp.core.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TicketInsufficientQuantityException extends RuntimeException {
    private final UUID ticketId;
    private final UUID orderId;

    public TicketInsufficientQuantityException(UUID ticketId, UUID orderId) {
        super("Ticket " + ticketId + " has insufficient quantity in the stock for order " + orderId);
        this.ticketId = ticketId;
        this.orderId = orderId;
    }
}
