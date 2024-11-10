package com.sp.ordersmicroservice.service;

import com.sp.core.dto.Order;
import com.sp.core.dto.event.OrderApprovedEvent;
import com.sp.core.dto.event.OrderCreatedEvent;
import com.sp.core.type.OrderStatus;
import com.sp.ordersmicroservice.dao.jpa.entity.OrderEntity;
import com.sp.ordersmicroservice.dao.jpa.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String ordersEventsTopicName;

    public OrderServiceImpl(OrderRepository orderRepository, KafkaTemplate<String, Object> kafkaTemplate,
                            @Value("${orders.events.topic.name}") String ordersEventsTopicName) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.ordersEventsTopicName = ordersEventsTopicName;
    }

    @Override
    public Order placeOrder(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setCustomerId(order.getCustomerId());
        entity.setTicketId(order.getTicketId());
        entity.setTicketQuantity(order.getTicketQuantity());
        entity.setStatus(OrderStatus.CREATED);
        orderRepository.save(entity);

        OrderCreatedEvent placeOrder = new OrderCreatedEvent(
                entity.getId(),
                entity.getCustomerId(),
                order.getTicketId(),
                order.getTicketQuantity()
        );

        kafkaTemplate.send(ordersEventsTopicName, placeOrder);


        return new Order(
                entity.getId(),
                entity.getCustomerId(),
                entity.getTicketId(),
                entity.getTicketQuantity(),
                entity.getStatus());
    }

    @Override
    public void approveOrder(UUID orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElse(null);
        Assert.notNull(order, "Order not found");

        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);

        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(orderId);
        kafkaTemplate.send(ordersEventsTopicName,orderApprovedEvent);
    }

    @Override
    public void rejectOrder(UUID orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElse(null);
        Assert.notNull(order, "Order not found");

        order.setStatus(OrderStatus.REJECTED);
        orderRepository.save(order);
    }

}
