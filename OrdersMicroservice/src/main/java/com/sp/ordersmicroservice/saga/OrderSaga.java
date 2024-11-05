package com.sp.ordersmicroservice.saga;

import com.sp.core.dto.command.ProcessPaymentCommand;
import com.sp.core.dto.command.ReserveTicketCommand;
import com.sp.core.dto.event.OrderCreatedEvent;
import com.sp.core.dto.event.TicketReservedEvent;
import com.sp.core.type.OrderStatus;
import com.sp.ordersmicroservice.service.OrderHistoryService;
import com.sp.ordersmicroservice.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {"${orders.events.topic.name}","${tickets.events.topic.name}"})
public class OrderSaga {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final OrderHistoryService orderHistoryService;
    private final String ticketCommandsTopicName;
    private final String paymentCommandsTopicName;

    public OrderSaga(KafkaTemplate<String, Object> kafkaTemplate, OrderHistoryService orderHistoryService,
                     @Value("${tickets.commands.topic.name}") String ticketCommandsTopicName,
                     @Value("${payments.commands.topic.name}") String paymentCommandsTopicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderHistoryService = orderHistoryService;
        this.ticketCommandsTopicName = ticketCommandsTopicName;
        this.paymentCommandsTopicName = paymentCommandsTopicName;
    }

    @KafkaHandler
    public void handler(@Payload OrderCreatedEvent event){
        ReserveTicketCommand command = new ReserveTicketCommand();
        BeanUtils.copyProperties(event,command);

        kafkaTemplate.send(ticketCommandsTopicName,command);
        orderHistoryService.add(event.getOrderId(), OrderStatus.CREATED);
    }

    @KafkaHandler
    public void handler(@Payload TicketReservedEvent event){
        ProcessPaymentCommand processPaymentCommand = new ProcessPaymentCommand(
                event.getOrderId(),
                event.getTicketId(),
                event.getTicketPrice(),
                event.getTicketQuantity()
        );

        kafkaTemplate.send(paymentCommandsTopicName,processPaymentCommand);
    }
}
