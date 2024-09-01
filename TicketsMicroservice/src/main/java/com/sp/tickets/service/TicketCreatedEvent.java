package com.sp.tickets.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
//no args constructor use for deserialize message in consumer microservice
public class TicketCreatedEvent {
    private String ticketId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
