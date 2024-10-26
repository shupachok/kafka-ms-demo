package com.sp.emailnotificationmicroservice;

import com.sp.core.event.TicketCreatedEvent;
import com.sp.emailnotificationmicroservice.handler.TicketCreatedEventHandler;
import com.sp.emailnotificationmicroservice.io.ProcessdEventRepository;
import com.sp.emailnotificationmicroservice.io.ProcessedEventEntity;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@EmbeddedKafka(partitions = 3,count = 3,controlledShutdown = true)
@SpringBootTest(properties = "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class TicketCreatedEventHandleIntegrationTest {

    @MockBean
    ProcessdEventRepository processdEventRepository;

    @MockBean
    RestTemplate restTemplate;

    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;

    @SpyBean
    TicketCreatedEventHandler ticketCreatedEventHandler;

    @Test
    public void testTicketCreatedEventHandler_OnTicketCreated_HandlesEvent() throws JSONException, ExecutionException, InterruptedException {

        //Arrange
        TicketCreatedEvent ticketCreatedEvent = new TicketCreatedEvent();
        ticketCreatedEvent.setPrice(new BigDecimal(10));
        ticketCreatedEvent.setTicketId(UUID.randomUUID().toString());
        ticketCreatedEvent.setQuantity(1);
        ticketCreatedEvent.setTitle("Non Concert");

        String messageId = UUID.randomUUID().toString();
        String messageKey = ticketCreatedEvent.getTicketId();

        ProducerRecord<String, Object> record = new ProducerRecord<>(
                "ticket-created-events-topic",
                messageKey,
                ticketCreatedEvent
        );

        record.headers().add("messageId", messageId.getBytes());
        record.headers().add(KafkaHeaders.RECEIVED_KEY, messageKey.getBytes());

//        ProcessedEventEntity processedEventEntity = new ProcessedEventEntity();
//        Optional<ProcessedEventEntity> mockupEntity = Optional.of(processedEventEntity);

        Optional<ProcessedEventEntity> mockupEntity = Optional.empty();

        when(processdEventRepository.findByMessageId(anyString())).thenReturn(mockupEntity);
        when(processdEventRepository.save(any(ProcessedEventEntity.class))).thenReturn(null);

        JSONObject body = new JSONObject();
        body.put("key","value");
        String responseBody = body.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, headers, HttpStatus.OK);

        when(restTemplate.exchange(any(String.class),
                any(HttpMethod.class),
                isNull(),
                eq(String.class))).thenReturn(responseEntity);

        //Act
        kafkaTemplate.send(record).get();

        // Assert
        ArgumentCaptor<String> messageIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TicketCreatedEvent> eventCaptor = ArgumentCaptor.forClass(TicketCreatedEvent.class);

        verify(ticketCreatedEventHandler, timeout(5000).times(1)).handle(eventCaptor.capture(),
                messageIdCaptor.capture(),
                messageKeyCaptor.capture());

        assertEquals(messageId,messageIdCaptor.getValue());
        assertEquals(messageKey, messageKeyCaptor.getValue());
        assertEquals(ticketCreatedEvent.getTicketId(),eventCaptor.getValue().getTicketId());

    }
}
