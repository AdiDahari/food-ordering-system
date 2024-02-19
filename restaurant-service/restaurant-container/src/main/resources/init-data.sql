INSERT INTO restaurant.restaurants(id, name, active)
    VALUES ('fd2cf964-7b9d-4f9e-a3c1-9d59137c8a45', 'restaurant_1', TRUE);
INSERT INTO restaurant.restaurants(id, name, active)
    VALUES ('fd2cf964-7b9d-4f9e-a3c1-9d59137c8a46', 'restaurant_2', FALSE);


INSERT INTO restaurant.products(id, name, price, available)
    VALUES ('fd2cf964-7b9d-4f9e-a3c1-9d59137c8a47', 'product_1', 25.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
    VALUES ('fd2cf964-7b9d-4f9e-a3c1-9d59137c8a48', 'product_2', 50.00, TRUE);
INSERT INTO restaurant.products(id, name, price, available)
    VALUES ('fd2cf964-7b9d-4f9e-a3c1-9d59137c8a49', 'product_3', 20.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
    VALUES ('fd2cf964-7b9d-4f9e-a3c1-9d59137c8a50', 'product_4', 40.00, TRUE);

INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
    VALUES ('fd2cf964-7b9d-4f9e-a3c1-9d59137c8a51', 'fd2cf964-7b9d-4f9e-a3c1-9d59137c8a45', 'fd2cf964-7b9d-4f9e-a3c1-9d59137c8a47');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
    VALUES ('fd2cf964-7b9d-4f9e-a3c1-9d59137c8a52', 'fd2cf964-7b9d-4f9e-a3c1-9d59137c8a45', 'fd2cf964-7b9d-4f9e-a3c1-9d59137c8a48');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
    VALUES ('fd2cf964-7b9d-4f9e-a3c1-9d59137c8a53', 'fd2cf964-7b9d-4f9e-a3c1-9d59137c8a46', 'fd2cf964-7b9d-4f9e-a3c1-9d59137c8a49');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
    VALUES ('fd2cf964-7b9d-4f9e-a3c1-9d59137c8a54', 'fd2cf964-7b9d-4f9e-a3c1-9d59137c8a46', 'fd2cf964-7b9d-4f9e-a3c1-9d59137c8a50');