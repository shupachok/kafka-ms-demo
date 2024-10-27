package com.sp.tickets.service;

import com.sp.core.dto.event.TicketCreatedEvent;
import com.sp.tickets.dao.jpa.entity.TicketEntity;
import com.sp.tickets.dao.repository.TicketRepository;
import com.sp.tickets.rest.CreateTicketRestModel;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {
    TicketRepository ticketRepository;
    KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public TicketServiceImpl(KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate,
                             TicketRepository ticketRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public String createTicket(CreateTicketRestModel createTicketRestModel) throws Exception {
        TicketEntity ticketEntity = new TicketEntity();
        BeanUtils.copyProperties(createTicketRestModel, ticketEntity);
        ticketRepository.save(ticketEntity);

        String ticketId = ticketEntity.getId().toString();

        // TODO:persist ticket detail into db before publish event
        TicketCreatedEvent ticketCreatedEvent = new TicketCreatedEvent(ticketId,
                createTicketRestModel.getTitle(),
                createTicketRestModel.getPrice(),
                createTicketRestModel.getQuantity());
        ProducerRecord<String,TicketCreatedEvent> record = new ProducerRecord<>(
                "ticket-created-events-topic",
                        ticketId,
                        ticketCreatedEvent);
//        record.headers().add("messageId","1234".getBytes());
        record.headers().add("messageId",UUID.randomUUID().toString().getBytes());

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
//        LOGGER.info("Before publishing Ticket created event");
//        SendResult<String, TicketCreatedEvent> result = kafkaTemplate.send("ticket-created-events-topic", ticketId, ticketCreatedEvent).get();

//        2.1 send synchronously with message header
        LOGGER.info("Before publishing Ticket created event");
        SendResult<String, TicketCreatedEvent> result = kafkaTemplate.send(record).get();


        LOGGER.info("Partition : {}", result.getRecordMetadata().partition());
        LOGGER.info("Topic : {}", result.getRecordMetadata().topic());
        LOGGER.info("Offset : {}", result.getRecordMetadata().offset());

        LOGGER.info("### Returning ticket id ###");

        return ticketId;
    }
}
