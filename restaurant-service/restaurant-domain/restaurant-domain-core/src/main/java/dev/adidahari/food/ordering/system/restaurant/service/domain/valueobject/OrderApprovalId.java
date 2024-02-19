package dev.adidahari.food.ordering.system.restaurant.service.domain.valueobject;

import dev.adidahari.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {
    public OrderApprovalId(UUID value) {
        super(value);
    }
}
