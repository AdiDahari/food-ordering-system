package dev.adidahari.food.ordering.system.order.service.domain;

import dev.adidahari.food.ordering.system.order.service.domain.entity.Order;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Restaurant;
import dev.adidahari.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import dev.adidahari.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import dev.adidahari.food.ordering.system.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

    OrderPaidEvent payOrder(Order order);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void cancelOrder(Order order, List<String> failureMessages);
}
