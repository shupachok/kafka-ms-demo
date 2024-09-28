package com.sp.emailnotificationmicroservice.handler;

import com.sp.core.TicketCreatedEvent;
import com.sp.emailnotificationmicroservice.error.NotRetryableException;
import com.sp.emailnotificationmicroservice.error.RetryableException;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

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
    }
}
