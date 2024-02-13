package dev.adidahari.food.ordering.system.order.service.domain;

import dev.adidahari.food.ordering.system.order.service.domain.entity.Order;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Product;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Restaurant;
import dev.adidahari.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import dev.adidahari.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import dev.adidahari.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import dev.adidahari.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService{

    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);

        order.validateOrder();
        order.initializeOrder();

        log.info("Order with id: {} is initiated", order.getId().getValue());

        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();

        log.info("Order with id: {} is paid", order.getId().getValue());

        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();

        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);

        log.info("Order with id: {} is cancelling",
                order.getId().getValue());

        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);

        log.info("Order with id: {} is cancelled", order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id: " + restaurant.getId().getValue() + " is currently inactive!");
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().forEach(orderItem -> {
                    Product currentProduct = orderItem.getProduct();

                    Product restaurantProduct = restaurant.getProductById(orderItem.getProduct().getId().getValue());

                    if (restaurantProduct != null) {
                        currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
                    }
                }
//                restaurant.getProducts().forEach(restaurantProduct -> {
//            Product currentProduct = orderItem.getProduct();
//            if (currentProduct.equals(restaurantProduct)) {
//                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),
//                        restaurantProduct.getPrice());
//            }
//        })
        );
    }

}
