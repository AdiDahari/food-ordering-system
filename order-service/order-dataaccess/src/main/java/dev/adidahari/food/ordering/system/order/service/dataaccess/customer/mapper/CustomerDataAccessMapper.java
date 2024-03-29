package dev.adidahari.food.ordering.system.order.service.dataaccess.customer.mapper;

import dev.adidahari.food.ordering.system.domain.valueobject.CustomerId;
import dev.adidahari.food.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity;
import dev.adidahari.food.ordering.system.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }
}
