package com.sp.ordersmicroservice.dao.jpa.entity;

import com.sp.core.type.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Table(name = "orders")
@Entity
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "customer_id")
    private UUID customerId;
    @Column(name = "ticket_id")
    private UUID ticketId;
    @Column(name = "ticket_quantity")
    private Integer ticketQuantity;
}
