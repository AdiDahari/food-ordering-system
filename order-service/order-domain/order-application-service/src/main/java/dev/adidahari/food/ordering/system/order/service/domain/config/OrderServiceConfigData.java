package dev.adidahari.food.ordering.system.order.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "order-service")
public class OrderServiceConfigData {
    private String PaymentRequestTopicName;
    private String PaymentResponseTopicName;
    private String restaurantApprovalRequestTopicName;
    private String restaurantApprovalResponseTopicName;

}
