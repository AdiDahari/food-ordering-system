package dev.adidahari.food.ordering.system.domain.event;

public interface DomainEvent<T> {
    void fire();
}
