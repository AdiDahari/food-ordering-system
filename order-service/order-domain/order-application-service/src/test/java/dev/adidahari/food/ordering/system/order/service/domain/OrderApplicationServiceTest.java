package dev.adidahari.food.ordering.system.order.service.domain;

import dev.adidahari.food.ordering.system.domain.valueobject.*;
import dev.adidahari.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import dev.adidahari.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import dev.adidahari.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import dev.adidahari.food.ordering.system.order.service.domain.dto.create.OrderItem;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Customer;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Order;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Product;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Restaurant;
import dev.adidahari.food.ordering.system.order.service.domain.exception.OrderDomainException;
import dev.adidahari.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import dev.adidahari.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import dev.adidahari.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import dev.adidahari.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import dev.adidahari.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {
    @Autowired
    private OrderApplicationService orderApplicationService;
    @Autowired
    private OrderDataMapper orderDataMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("8faadaa4-7bfb-46e4-80c7-4d172accc8c6");
    private final UUID PRODUCT_ID = UUID.fromString("5b41d8df-924f-4c2b-9a05-3d7b8b46899c");
    private final UUID RESTAURANT_ID = UUID.fromString("20b92ba4-d3b9-4345-bf94-7b2ccab8ec3e");
    private final UUID ORDER_ID = UUID.fromString("f42ad445-2a07-4da4-b7eb-6d15723a0a82");
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    public void init() {
        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .orderAddress(OrderAddress.builder()
                        .street("Sesame St.")
                        .postalCode("0101010")
                        .city("Paris")
                        .build())
                .price(PRICE)
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(2)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("100.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .orderAddress(OrderAddress.builder()
                        .street("Sesame St.")
                        .postalCode("0101010")
                        .city("Paris")
                        .build())
                .price(PRICE)
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();
        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .orderAddress(OrderAddress.builder()
                        .street("Sesame St.")
                        .postalCode("0101010")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("210.00"))
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("60.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(new Product(
                                new ProductId(PRODUCT_ID),
                                "product-1",
                                new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID),
                                "product-2",
                                new Money(new BigDecimal("50.00")))))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));

        when(orderRepository.save(any(Order.class))).thenReturn(order);
    }

    @Test
    public void testCreateOrder() {
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);

        assertEquals(OrderStatus.PENDING, createOrderResponse.getOrderStatus());
        assertEquals("Order Created Successfully", createOrderResponse.getMessage());
        assertNotNull(createOrderResponse.getOrderTrackingId());

    }
    @Test
    public void testCreateOrderWithWrongTotalPrice() {

        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        assertEquals("Total price: 250.00 do not match to order items' total: 200.00", orderDomainException.getMessage());
    }

    @Test
    public void testCreateOrderWithWrongProductPrice() {

        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        assertEquals("Order item price: 60.00 is invalid for product: " + PRODUCT_ID, orderDomainException.getMessage());
    }

    @Test
    public void testCreateOrderWithInactiveRestaurant() {
        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(new Product(
                                new ProductId(PRODUCT_ID),
                                "product-1",
                                new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID),
                                "product-2",
                                new Money(new BigDecimal("50.00")))))
                .active(false)
                .build();

        when(restaurantRepository.findRestaurantInformation(orderDataMapper
                .createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));

        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
        () -> orderApplicationService.createOrder(createOrderCommand));

        assertEquals("Restaurant with id: " + RESTAURANT_ID + " is currently inactive!", orderDomainException.getMessage());
    }

}
