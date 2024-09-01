package com.sp.tickets.rest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTicketRestModel {
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
