package com.sp.tickets;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@DirtiesContext
//clean state before test
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//TestInstance.Lifecycle.PER_CLASS = create only one instance for test in this class
//by default Junit will create instance per method (4 method 4 instant)
//@ActiveProfiles("test")
@EmbeddedKafka(partitions = 3,count = 3,controlledShutdown = true)
//count = number of broker
//controlledShutdown = migrate all leader to another broker before it shutting down
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class TicketServiceIntegrationTest {

    //naming convention: test<System Under Test>_Condition or State change_ExpectedResult
    @Test
    void testCreateTicket_whenGivenValidTicketDetails_successfulSendsKafkaMessage(){

    }
}
