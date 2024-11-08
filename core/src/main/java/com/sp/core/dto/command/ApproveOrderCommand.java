package com.sp.core.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ApproveOrderCommand {
    private UUID orderId;
}
