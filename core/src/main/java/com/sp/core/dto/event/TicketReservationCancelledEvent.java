package com.sp.core.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketReservationCancelledEvent {
    UUID ticketId;
    UUID orderId;
}
