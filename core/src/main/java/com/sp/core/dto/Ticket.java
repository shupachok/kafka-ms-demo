package com.sp.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Integer quantity;

    public Ticket(UUID productId, Integer quantity) {
        this.id = productId;
        this.quantity = quantity;
    }
}