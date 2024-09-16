package com.sp.emailnotificationmicroservice.handler;

import com.sp.core.TicketCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
//    multiple topics
//    @KafkaListener(topics = {"ticket-created-events-topic-1","ticket-created-events-topic-2"})
@KafkaListener(topics = "ticket-created-events-topic")
public class TicketCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @KafkaHandler
    public void handle(TicketCreatedEvent ticketCreatedEvent){
        LOGGER.info("Received ticket created event: " + ticketCreatedEvent.getTitle());
    }
}
