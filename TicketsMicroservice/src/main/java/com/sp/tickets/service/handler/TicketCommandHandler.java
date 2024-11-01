package com.sp.tickets.service.handler;

import com.sp.core.dto.Ticket;
import com.sp.core.dto.command.ReserveTicketCommand;
import com.sp.tickets.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@KafkaListener(topics = "${tickets.commands.topic.name}")
public class TicketCommandHandler {

    private final TicketService ticketService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TicketCommandHandler(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @KafkaHandler
    public void handleCommand(@Payload ReserveTicketCommand command){
        try {
            Ticket desiredTicket = new Ticket(command.getTicketId(), command.getTicketQuantity());
            Ticket reservedTicket = ticketService.reserve(desiredTicket,command.getOrderId());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(),e);
        }
    }
}
