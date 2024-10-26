package com.sp.ordersmicroservice.service;

import com.sp.core.dto.Order;
import com.sp.core.type.OrderStatus;
import com.sp.ordersmicroservice.dao.jpa.entity.OrderEntity;
import com.sp.ordersmicroservice.dao.jpa.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order placeOrder(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setCustomerId(order.getCustomerId());
        entity.setTicketId(order.getTicketId());
        entity.setTicketQuantity(order.getTicketQuantity());
        entity.setStatus(OrderStatus.CREATED);
        orderRepository.save(entity);

        return new Order(
                entity.getId(),
                entity.getCustomerId(),
                entity.getTicketId(),
                entity.getTicketQuantity(),
                entity.getStatus());
    }

}
