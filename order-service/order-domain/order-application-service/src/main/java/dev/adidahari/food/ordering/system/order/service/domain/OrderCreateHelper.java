package dev.adidahari.food.ordering.system.order.service.domain;

import dev.adidahari.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Customer;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Order;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Restaurant;
import dev.adidahari.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import dev.adidahari.food.ordering.system.order.service.domain.exception.OrderDomainException;
import dev.adidahari.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import dev.adidahari.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import dev.adidahari.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import dev.adidahari.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import dev.adidahari.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateHelper {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedEventDomainEventPublisher;

    public OrderCreateHelper(OrderDomainService orderDomainService,
                             OrderRepository orderRepository,
                             CustomerRepository customerRepository,
                             RestaurantRepository restaurantRepository,
                             OrderDataMapper orderDataMapper,
                             OrderCreatedPaymentRequestMessagePublisher orderCreatedEventDomainEventPublisher) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedEventDomainEventPublisher = orderCreatedEventDomainEventPublisher;
    }

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());

        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);

        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant,
                orderCreatedEventDomainEventPublisher);

        saveOrder(order);

        log.info("Order created with id: {}", orderCreatedEvent.getOrder().getId().getValue());

        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);

        if (optionalRestaurant.isEmpty()) {
            log.warn("Could not find a restaurant with id: {}", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Could not find a restaurant with id: " +
                    createOrderCommand.getRestaurantId());
        }

        return optionalRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not fin a customer with id: {}", customerId);
            throw new OrderDomainException("Could not fin a customer with id: " + customerId);
        }
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if (orderResult == null) {
            log.error("Failed to save order");
            throw new OrderDomainException("Failed to save order");
        }
        log.info("Order with id: {} is saved", orderResult.getId().getValue());
        return orderResult;
    }


}
