package com.sp.tickets.service;

import com.sp.core.TicketCreatedEvent;
import com.sp.tickets.rest.CreateTicketRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public TicketServiceImpl(KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createTicket(CreateTicketRestModel createTicketRestModel) throws Exception {
        String ticketId = UUID.randomUUID().toString();

        // TODO:persist ticket detail into db before publish event
        TicketCreatedEvent ticketCreatedEvent = new TicketCreatedEvent(ticketId,
                createTicketRestModel.getTitle(),
                createTicketRestModel.getPrice(),
                createTicketRestModel.getQuantity());

//      sent asynchronously
//        CompletableFuture<SendResult<String,TicketCreatedEvent>> future = kafkaTemplate.send("ticket-created-events-topic",ticketId,ticketCreatedEvent);
//
//        future.whenComplete((result,exception) -> {
//            if (exception != null) {
//                LOGGER.error("Failed to send message: {}", exception.getMessage());
//            } else {
//                LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
//            }
//        });

//        1.sent synchronously with join (add join after asynchronously to wait for result from future operation)
//        future.join();

//        2.sent synchronously
        LOGGER.info("Before publishing Ticket created event");
        SendResult<String, TicketCreatedEvent> result = kafkaTemplate.send("ticket-created-events-topic", ticketId, ticketCreatedEvent).get();

        LOGGER.info("Partition : {}", result.getRecordMetadata().partition());
        LOGGER.info("Topic : {}", result.getRecordMetadata().topic());
        LOGGER.info("Offset : {}", result.getRecordMetadata().offset());

        LOGGER.info("### Returning product id ###");

        return ticketId;
    }
}
