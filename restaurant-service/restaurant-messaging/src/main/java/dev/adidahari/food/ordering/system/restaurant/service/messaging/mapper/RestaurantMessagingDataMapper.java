package dev.adidahari.food.ordering.system.restaurant.service.messaging.mapper;

import dev.adidahari.food.ordering.system.domain.valueobject.ProductId;
import dev.adidahari.food.ordering.system.domain.valueobject.RestaurantOrderStatus;
import dev.adidahari.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import dev.adidahari.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import dev.adidahari.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import dev.adidahari.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import dev.adidahari.food.ordering.system.restaurant.service.domain.entity.Product;
import dev.adidahari.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import dev.adidahari.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantMessagingDataMapper {
    public RestaurantApprovalResponseAvroModel orderApprovedEventTORestaurantApprovalResponseAvroModel(
            OrderApprovalEvent orderApprovalEvent
    ) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(orderApprovalEvent.getOrderApproval().getOrderId().getValue().toString())
                .setRestaurantId(orderApprovalEvent.getRestaurantId().getValue().toString())
                .setCreatedAt(orderApprovalEvent.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus
                        .valueOf(orderApprovalEvent.getOrderApproval().getApprovalStatus().name()))
                .setFaliureMessages(orderApprovalEvent.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponseAvroModel orderRejectedEventToRestaurantApprovalResponseAvroModel(
            OrderRejectedEvent orderRejectedEvent
    ) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(orderRejectedEvent.getOrderApproval().getOrderId().getValue().toString())
                .setRestaurantId(orderRejectedEvent.getRestaurantId().getValue().toString())
                .setCreatedAt(orderRejectedEvent.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus
                        .valueOf(orderRejectedEvent.getOrderApproval().getApprovalStatus().name()))
                .setFaliureMessages(orderRejectedEvent.getFailureMessages())
                .build();
    }

    public RestaurantApprovalRequest restaurantApprovalRequestAvroModelToRestaurantApproval(
            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel
    ) {
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
                .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId())
                .orderId(restaurantApprovalRequestAvroModel.getOrderId())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel
                        .getRestaurantOrderStatus().name()))
                .products(restaurantApprovalRequestAvroModel.getProducts().stream()
                        .map(avroModel -> Product.builder()
                                .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                .quantity(avroModel.getQuantity())
                                .build())
                        .toList())
                .price(restaurantApprovalRequestAvroModel.getPrice())
                .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
                .build();
    }
}
