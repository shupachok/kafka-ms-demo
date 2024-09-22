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
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
//    multiple topics
//    @KafkaListener(topics = {"ticket-created-events-topic-1","ticket-created-events-topic-2"})
@KafkaListener(topics = "ticket-created-events-topic")
public class TicketCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @KafkaHandler
    public void handle(TicketCreatedEvent ticketCreatedEvent){
        LOGGER.info("Received ticket created event: " + ticketCreatedEvent.getTitle());

        String emailServerUrl = "http://localhost:8083/response/500";

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
