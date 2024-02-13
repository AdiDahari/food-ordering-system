package dev.adidahari.food.ordering.system.domain.event.publisher;

import dev.adidahari.food.ordering.system.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
