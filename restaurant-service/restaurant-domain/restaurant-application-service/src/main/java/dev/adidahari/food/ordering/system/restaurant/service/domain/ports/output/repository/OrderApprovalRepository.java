package dev.adidahari.food.ordering.system.restaurant.service.domain.ports.output.repository;

import dev.adidahari.food.ordering.system.restaurant.service.domain.entity.OrderApproval;

public interface OrderApprovalRepository {
    OrderApproval save(OrderApproval orderApproval);
}
