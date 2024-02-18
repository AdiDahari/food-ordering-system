package dev.adidahari.food.ordering.system.order.service.domain.entity;

import dev.adidahari.food.ordering.system.domain.entity.AggregateRoot;
import dev.adidahari.food.ordering.system.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    public Customer() {
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
}
