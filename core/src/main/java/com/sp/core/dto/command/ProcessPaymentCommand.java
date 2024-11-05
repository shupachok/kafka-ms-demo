package com.sp.core.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcessPaymentCommand {
    private UUID orderId;
    private UUID ticketId;
    private BigDecimal ticketPrice;
    private Integer ticketQuantity;
}
