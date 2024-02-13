package dev.adidahari.food.ordering.system.order.service.domain.exception;

import dev.adidahari.food.ordering.system.domain.exception.DomainException;

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
