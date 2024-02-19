package dev.adidahari.food.ordering.system.restaurant.service.domain.ports.output.message.publisher;

import dev.adidahari.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import dev.adidahari.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import dev.adidahari.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {

}
