package com.sp.tickets.dao.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "tickets")
@Entity
@Data
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
