package dev.adidahari.food.ordering.system.order.service.domain.event;

import dev.adidahari.food.ordering.system.domain.event.DomainEvent;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {
    public OrderCancelledEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
