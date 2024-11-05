package com.sp.paymentsmicroservice.service.handler;

import com.sp.core.dto.Payment;
import com.sp.core.dto.command.ProcessPaymentCommand;
import com.sp.core.dto.event.PaymentFailedEvent;
import com.sp.core.dto.event.PaymentProcessedEvent;
import com.sp.core.exception.CreditCardProcessorUnavailableException;
import com.sp.paymentsmicroservice.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${payments.commands.topic.name}")
public class PaymentCommandHandler {

    private final PaymentService paymentService;
    private final String paymentEventsTopicName;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public PaymentCommandHandler(PaymentService paymentService,
                                 @Value("${payments.events.topic.name}") String paymentEventsTopicName,
                                 KafkaTemplate<String, Object> kafkaTemplate) {
        this.paymentService = paymentService;
        this.paymentEventsTopicName = paymentEventsTopicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaHandler
    public void handleCommand(@Payload ProcessPaymentCommand command) {
        try {
            Payment payment = new Payment(command.getOrderId(),
                    command.getTicketId(),
                    command.getTicketPrice(),
                    command.getTicketQuantity());

            Payment process = paymentService.process(payment);
            PaymentProcessedEvent event = new PaymentProcessedEvent(process.getOrderId(),process.getId());
            kafkaTemplate.send(paymentEventsTopicName,process);

        } catch (CreditCardProcessorUnavailableException e) {
            LOGGER.error(e.getLocalizedMessage(),e);
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(command.getOrderId(), command.getTicketId(), command.getTicketQuantity());
            kafkaTemplate.send(paymentEventsTopicName,paymentFailedEvent);
        }

    }
}
