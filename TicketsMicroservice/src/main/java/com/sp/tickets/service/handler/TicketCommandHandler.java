package com.sp.tickets.service.handler;

import com.sp.core.dto.Ticket;
import com.sp.core.dto.command.CancelTicketReservationCommand;
import com.sp.core.dto.command.ReserveTicketCommand;
import com.sp.core.dto.event.TicketReservationCancelledEvent;
import com.sp.core.dto.event.TicketReservationFailedEvent;
import com.sp.core.dto.event.TicketReservedEvent;
import com.sp.tickets.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@KafkaListener(topics = "${tickets.commands.topic.name}")
public class TicketCommandHandler {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    KafkaTemplate<String,Object> kafkaTemplate;
    private String ticketEventsTopicName;
    private TicketService ticketService;

    public TicketCommandHandler(KafkaTemplate<String, Object> kafkaTemplate,
                                TicketService ticketService,
                                @Value("${tickets.events.topic.name}") String ticketEventsTopicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.ticketService = ticketService;
        this.ticketEventsTopicName = ticketEventsTopicName;
    }
//    public TicketCommandHandler(KafkaTemplate<String, Object> kafkaTemplate,TicketService ticketService,
//                                @Value("${tickets.events.topic.name}") String ticketEventsTopicName) {
//        this.ticketService = ticketService;
//        this.kafkaTemplate = kafkaTemplate;
//        this.ticketEventsTopicName = ticketEventsTopicName;
//    }

    @KafkaHandler
    public void handleCommand(@Payload ReserveTicketCommand command){
        try {
            Ticket desiredTicket = new Ticket(command.getTicketId(), command.getTicketQuantity());
            Ticket reservedTicket = ticketService.reserve(desiredTicket,command.getOrderId());
            TicketReservedEvent ticketReservedEvent = new TicketReservedEvent(command.getOrderId(),
                    command.getTicketId(),
                    reservedTicket.getPrice(),
                    command.getTicketQuantity());
            kafkaTemplate.send(ticketEventsTopicName,ticketReservedEvent);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(),e);
            TicketReservationFailedEvent ticketReservationFailedEvent = new TicketReservationFailedEvent(
                    command.getTicketId(),command.getOrderId(),command.getTicketQuantity());
            kafkaTemplate.send(ticketEventsTopicName,ticketReservationFailedEvent);
        }
    }

    @KafkaHandler
    public void handleCommand(@Payload CancelTicketReservationCommand command){
        Ticket ticketToCancel = new Ticket(command.getTicketId(), command.getTicketQuantity());
        ticketService.cancelReservation(ticketToCancel,command.getOrderId());

        TicketReservationCancelledEvent ticketReservationCancelledEvent = new TicketReservationCancelledEvent(command.getTicketId(), command.getOrderId());
        kafkaTemplate.send(ticketEventsTopicName,ticketReservationCancelledEvent);
    }
}
