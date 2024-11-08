package com.sp.ordersmicroservice.service.handler;

import com.sp.core.dto.command.ApproveOrderCommand;
import com.sp.ordersmicroservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${orders.commands.topic.name}")
public class OrderCommandHandler {

    @Autowired
    OrderService orderService;

    @KafkaHandler
    public void handleCommand(@Payload ApproveOrderCommand approveOrderCommand){
        orderService.approveOrder(approveOrderCommand.getOrderId());
    }
}
