package com.food.order.system.data.access.order.mapper;

import com.food.order.system.data.access.order.entity.OrderAddressEntity;
import com.food.order.system.data.access.order.entity.OrderEntity;
import com.food.order.system.data.access.order.entity.OrderItemEntity;
import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.entity.OrderItem;
import com.food.order.system.domain.entity.Product;
import com.food.order.system.domain.valueobject.OrderItemId;
import com.food.order.system.domain.valueobject.StreetAddress;
import com.food.order.system.domain.valueobject.TrackingId;
import com.food.order.system.valueobject.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.food.order.system.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId().getValue());
        orderEntity.setOrderStatus(order.getStatus());
        orderEntity.setCustomerId(order.getCustomerId().getValue());
        orderEntity.setRestaurantId(order.getRestaurantId().getValue());
        orderEntity.setTrackingId(order.getTrackingId().getValue());
        orderEntity.setAddress(deliveryAddressToAddressEntity(order.getDeliveryAddress()));
        orderEntity.setPrice(order.getPrice().getAmount());
        orderEntity.setItems(orderItemsToOrderItemsEntity(order.getItems()));
        orderEntity.setFailureMessages(Objects.nonNull(order.getFailureMessages()) ?
                String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "");
        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(item -> item.setOrderEntity(orderEntity));
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity){
        return Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .items(orderItemsEntityToOrderItems(orderEntity.getItems()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .status(orderEntity.getOrderStatus())
                .failureMessages(Objects.isNull(orderEntity.getFailureMessages())  ? new ArrayList<>() :
                        new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages()
                                .split(FAILURE_MESSAGE_DELIMITER))))
                .build();
    }

    private List<OrderItem> orderItemsEntityToOrderItems(List<OrderItemEntity> items) {
        return items.stream()
                .map(item -> OrderItem.builder()
                        .orderItemId(new OrderItemId(item.getId()))
                        .product(new Product(new ProductId(item.getProductId())))
                        .quantity(item.getQuantity())
                        .price(new Money(item.getPrice()))
                        .subTotal(new Money(item.getSubTotal()))
                        .build())
                .toList();
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
        return new StreetAddress(address.getId(),address.getStreet(), address.getCity() , address.getPostalCode());
    }

    private List<OrderItemEntity> orderItemsToOrderItemsEntity(List<OrderItem> items) {
        return items.stream()
                .map(item -> OrderItemEntity.builder()
                        .id(item.getId().getValue())
                        .productId(item.getProduct().getId().getValue())
                        .price(item.getPrice().getAmount())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal().getAmount())
                        .build())
                .toList();
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        OrderAddressEntity orderAddressEntity = new OrderAddressEntity();
        orderAddressEntity.setId(deliveryAddress.getId());
        orderAddressEntity.setStreet(deliveryAddress.getStreet());
        orderAddressEntity.setCity(deliveryAddress.getCity());
        orderAddressEntity.setPostalCode(deliveryAddress.getPostalCode());
        return orderAddressEntity;
    }
}
