package com.sp.paymentsmicroservice.dao.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "payments")
@Entity
@Data
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "ticket_id")
    private UUID ticketId;
    @Column(name = "ticket_price")
    private BigDecimal ticketPrice;
    @Column(name = "ticket_quantity")
    private Integer ticketQuantity;
}
