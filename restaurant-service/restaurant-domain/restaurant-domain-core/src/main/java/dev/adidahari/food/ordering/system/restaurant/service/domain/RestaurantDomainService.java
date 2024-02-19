package dev.adidahari.food.ordering.system.restaurant.service.domain;


import dev.adidahari.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import dev.adidahari.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import dev.adidahari.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import dev.adidahari.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import dev.adidahari.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;

import java.util.List;

public interface RestaurantDomainService {

    OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages,
                                     DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher,
                                     DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher);
}
