package com.sp.ordersmicroservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateOrderRequest {
    @NotNull
    private UUID customerId;
    @NotNull
    private UUID ticketId;
    @NotNull
    @Positive
    private Integer ticketQuantity;

}
