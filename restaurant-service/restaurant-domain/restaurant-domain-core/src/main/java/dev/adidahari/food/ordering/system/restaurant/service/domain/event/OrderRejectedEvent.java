package dev.adidahari.food.ordering.system.restaurant.service.domain.event;

import dev.adidahari.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import dev.adidahari.food.ordering.system.domain.valueobject.RestaurantId;
import dev.adidahari.food.ordering.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher;

    public OrderRejectedEvent(OrderApproval orderApproval, RestaurantId restaurantId,
                              List<String> failureMessages, ZonedDateTime createdAt,
                              DomainEventPublisher<OrderRejectedEvent> orderApprovedEventDomainEventPublisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.orderRejectedEventDomainEventPublisher = orderApprovedEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderRejectedEventDomainEventPublisher.publish(this);
    }
}
