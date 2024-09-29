package com.sp.emailnotificationmicroservice.handler;

import com.sp.core.TicketCreatedEvent;
import com.sp.emailnotificationmicroservice.error.NotRetryableException;
import com.sp.emailnotificationmicroservice.error.RetryableException;
import com.sp.emailnotificationmicroservice.io.ProcessdEventRepository;
import com.sp.emailnotificationmicroservice.io.ProcessedEventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@Component
//    multiple topics
//    @KafkaListener(topics = {"ticket-created-events-topic-1","ticket-created-events-topic-2"})
//    config group id
//    @KafkaListener(topics = "ticket-created-events-topic",groupId = "ticket-created-event")
@KafkaListener(topics = "ticket-created-events-topic")
public class TicketCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProcessdEventRepository processdEventRepository;

    @Transactional
    @KafkaHandler
    public void handle(@Payload TicketCreatedEvent ticketCreatedEvent,
                       @Header(value = "messageId",required = true) String messageId,
                       @Header(KafkaHeaders.RECEIVED_KEY) String messageKey){
        LOGGER.info("""
                    Received ticket created event title: {}
                    messageId: {}
                    messageKey: {}
                    ticketId: {}"""
                ,ticketCreatedEvent.getTitle() , messageId, messageKey, ticketCreatedEvent.getTicketId());

        Optional<ProcessedEventEntity> processedEvent = processdEventRepository.findByMessageId(messageId);
        if(processedEvent.isPresent()){
            LOGGER.error("MessageId: {} is already in processed event", messageId);
            return;
        }

        String emailServerUrl = "http://localhost:8083/response/200";

        try {
            ResponseEntity<String> response = restTemplate.exchange(emailServerUrl, HttpMethod.GET, null, String.class);
            if(response.getStatusCode().is2xxSuccessful()){
                LOGGER.info("Successfully call email server: " + response.getBody());
            }
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
            throw new RetryableException(e);
        } catch (HttpServerErrorException e) {
            LOGGER.error(e.getMessage());
            throw new NotRetryableException(e);
        } catch (Exception ex){
            LOGGER.error(ex.getMessage());
            throw new NotRetryableException(ex);
        }

        ProcessedEventEntity processedEventEntity = new ProcessedEventEntity();
        processedEventEntity.setMessageId(messageId);
        processedEventEntity.setTicketId(ticketCreatedEvent.getTicketId());

        try {
            processdEventRepository.save(processedEventEntity);
        } catch (Exception ex){
            throw new NotRetryableException(ex);
        }
    }
}
