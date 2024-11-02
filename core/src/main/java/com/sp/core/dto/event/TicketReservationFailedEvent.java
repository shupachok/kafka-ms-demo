package com.sp.core.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketReservationFailedEvent {
    private UUID ticketId;
    private UUID orderId;
    private Integer ticketQuantity;
}
