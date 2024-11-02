package com.sp.core.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketReservedEvent {
    private UUID orderId;
    private UUID ticketId;
    private BigDecimal ticketPrice;
    private Integer ticketQuantity;
}
