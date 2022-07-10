package com.food.order.domain.mapper;

import com.food.order.domain.dto.create.CreateOrderCommand;
import com.food.order.domain.dto.create.CreateOrderResponse;
import com.food.order.domain.dto.create.OrderAddress;
import com.food.order.domain.dto.track.TrackOrderResponse;
import com.food.order.domain.valueobject.CustomerId;
import com.food.order.domain.valueobject.Money;
import com.food.order.domain.valueobject.ProductId;
import com.food.order.domain.valueobject.RestaurantId;
import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.entity.OrderItem;
import com.food.order.system.domain.entity.Product;
import com.food.order.system.domain.entity.Restaurant;
import com.food.order.system.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {

        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }


    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .id(new RestaurantId(createOrderCommand.restaurantId()))
                .products(createOrderCommand.orderItems().stream()
                        .map(orderItem ->
                            new Product(new ProductId(orderItem.productId())))
                                    .toList()
                        )
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.customerId()))
                .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.orderAddress()))
                .price(new Money(createOrderCommand.price()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.orderItems()))
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<com.food.order.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem ->
                    OrderItem.builder()
                            .product(new Product(new ProductId(orderItem.productId())))
                            .price(new Money(orderItem.price()))
                            .quantity(orderItem.quantity())
                            .subTotal(new Money(orderItem.subTotal()))
                            .build())
        .collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.street(),
                orderAddress.city(),
                orderAddress.postalCode()
        );
    }


    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getStatus())
                .message(message)
                .build();
    }
}
