package com.sp.tickets;

import com.sp.core.dto.event.TicketCreatedEvent;
import com.sp.tickets.rest.CreateTicketRestModel;
import com.sp.tickets.service.TicketService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@DirtiesContext
//clean state before test
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//TestInstance.Lifecycle.PER_CLASS = create only one instance for test in this class
//by default Junit will create instance per method (4 method 4 instant)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 3,count = 3,controlledShutdown = true)
//count = number of broker
//controlledShutdown = migrate all leader to another broker before it shutting down

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class TicketServiceIntegrationTest {

    @Autowired
    private TicketService ticketService;

    /*
    Note:
    IntelliJ IDEA is reporting errors this line (Eclipse not reporting errors)
    but JUnit tests are still executing and passing successfully
    */
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private Environment environment;

    private KafkaMessageListenerContainer<String, TicketCreatedEvent> container;
    private BlockingQueue<ConsumerRecord<String, TicketCreatedEvent>> records;

    @BeforeAll
    void setUp() {
        DefaultKafkaConsumerFactory<String,Object> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProperties());

        ContainerProperties containerProperties = new ContainerProperties(environment.getProperty("ticket-created-events-topic-name"));
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingDeque<>();
        container.setupMessageListener((MessageListener<String, TicketCreatedEvent>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    //naming convention: test<System Under Test>_Condition or State change_ExpectedResult
    @Test
    void testCreateTicket_whenGivenValidTicketDetails_successfulSendsKafkaMessage() throws Exception {
        String title = "Non Concert";
        BigDecimal price = new BigDecimal(600);
        Integer quantity = 1;

        CreateTicketRestModel createTicketRestModel = new CreateTicketRestModel();
        createTicketRestModel.setTitle(title);
        createTicketRestModel.setPrice(price);
        createTicketRestModel.setQuantity(quantity);

        ticketService.createTicket(createTicketRestModel);

        ConsumerRecord<String, TicketCreatedEvent> message = records.poll(3000, TimeUnit.MILLISECONDS);
        assertNotNull(message);
        assertNotNull(message.key());
        TicketCreatedEvent ticketCreatedEvent = message.value();
        assertEquals(createTicketRestModel.getQuantity(),ticketCreatedEvent.getQuantity());
        assertEquals(createTicketRestModel.getTitle(),ticketCreatedEvent.getTitle());
        assertEquals(createTicketRestModel.getPrice(),ticketCreatedEvent.getPrice());

    }

    private Map<String,Object> getConsumerProperties(){
        return Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,JsonDeserializer.class,
                JsonDeserializer.TRUSTED_PACKAGES,environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"),
                ConsumerConfig.GROUP_ID_CONFIG,environment.getProperty("spring.kafka.consumer.group-id"),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,environment.getProperty("spring.kafka.consumer.auto-offset-reset"));
    }

    @AfterAll
    void tearDown(){
        container.stop();
    }


}
