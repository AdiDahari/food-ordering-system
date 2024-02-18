package dev.adidahari.food.ordering.system.payment.service.domain.valueobject;

import dev.adidahari.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {
    public PaymentId(UUID value) {
        super(value);
    }
}
