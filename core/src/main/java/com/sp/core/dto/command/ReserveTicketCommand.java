package com.sp.core.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveTicketCommand {
    private UUID orderId;
    private UUID ticketId;
    private Integer ticketQuantity;
}
