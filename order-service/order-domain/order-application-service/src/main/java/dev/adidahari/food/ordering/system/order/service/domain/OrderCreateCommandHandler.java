package dev.adidahari.food.ordering.system.order.service.domain;

import dev.adidahari.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import dev.adidahari.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Customer;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Restaurant;
import dev.adidahari.food.ordering.system.order.service.domain.exception.OrderDomainException;
import dev.adidahari.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
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
public class OrderCreateCommandHandler {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService,
                                     OrderRepository orderRepository,
                                     CustomerRepository customerRepository,
                                     RestaurantRepository restaurantRepository,
                                     OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        return null;
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not fin a customer with id: {}", customerId);
            throw new OrderDomainException("Could not fin a customer with id: " + customerId);
        }
    }
}
