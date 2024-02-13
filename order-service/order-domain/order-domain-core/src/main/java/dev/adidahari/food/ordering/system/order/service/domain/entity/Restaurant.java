package dev.adidahari.food.ordering.system.order.service.domain.entity;

import dev.adidahari.food.ordering.system.domain.entity.AggregateRoot;
import dev.adidahari.food.ordering.system.domain.valueobject.RestaurantId;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private final HashMap<UUID, Product> products;
    private boolean active;


    public Product getProductById(UUID id) {
        return products.get(id);
    }

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantId);
        products = builder.products;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public HashMap<UUID, Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private HashMap<UUID, Product> products;
        private boolean active;

        private Builder() {
        }


        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder products(HashMap<UUID, Product> val) {
            products = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
