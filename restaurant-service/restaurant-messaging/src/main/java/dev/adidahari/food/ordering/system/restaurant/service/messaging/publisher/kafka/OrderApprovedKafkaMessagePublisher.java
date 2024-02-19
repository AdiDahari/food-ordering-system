package dev.adidahari.food.ordering.system.restaurant.service.messaging.publisher.kafka;

import dev.adidahari.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import dev.adidahari.food.ordering.system.kafka.producer.KafkaMessageHelper;
import dev.adidahari.food.ordering.system.kafka.producer.service.KafkaProducer;
import dev.adidahari.food.ordering.system.restaurant.service.domain.config.RestaurantServiceConfigData;
import dev.adidahari.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import dev.adidahari.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import dev.adidahari.food.ordering.system.restaurant.service.messaging.mapper.RestaurantMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderApprovedKafkaMessagePublisher implements OrderApprovedMessagePublisher {
    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderApprovedKafkaMessagePublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper,
                                              KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
                                              RestaurantServiceConfigData restaurantServiceConfigData,
                                              KafkaMessageHelper kafkaMessageHelper) {
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }


    @Override
    public void publish(OrderApprovedEvent domainEvent) {
        String orderId = domainEvent.getOrderApproval().getOrderId().getValue().toString();

        log.info("Received OrderApprovedEvent for order id: {}", orderId);

        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel =
                    restaurantMessagingDataMapper.orderApprovedEventTORestaurantApprovalResponseAvroModel(domainEvent);

            kafkaProducer.send(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                    orderId,
                    restaurantApprovalResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                            restaurantApprovalResponseAvroModel,
                            orderId,
                            "RestaurantApprovalResponseAvroModel"));

            log.info("RestaurantApprovalResponseAvroModel sent to Kafka at: {}", System.nanoTime());
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel" +
                    " message to Kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
