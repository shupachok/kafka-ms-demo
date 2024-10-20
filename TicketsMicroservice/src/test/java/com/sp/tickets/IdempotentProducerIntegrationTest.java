package com.sp.tickets;

import com.sp.core.TicketCreatedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@SpringBootTest
public class IdempotentProducerIntegrationTest {

    @Autowired
    KafkaTemplate<String, TicketCreatedEvent> kafkaTemplate;

    @MockBean
    KafkaAdmin kafkaAdmin;

    @Test
    void testProducerConfig_whenIdempotenceEnabled_assertsIdempotentProperties() {
        ProducerFactory<String, TicketCreatedEvent> producerFactory = kafkaTemplate.getProducerFactory();

        Map<String, Object> config = producerFactory.getConfigurationProperties();

        Assertions.assertTrue(Boolean.valueOf((String) config.get(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG)));
        Assertions.assertTrue("all".equalsIgnoreCase((String) config.get(ProducerConfig.ACKS_CONFIG)));

        if (config.containsKey(ProducerConfig.RETRIES_CONFIG)) {
            Assertions.assertTrue(
                    Integer.parseInt(config.get(ProducerConfig.RETRIES_CONFIG).toString()) > 0
            );
        }
    }
}
