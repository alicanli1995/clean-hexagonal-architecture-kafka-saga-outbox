package com.food.order.system.mapper;

import com.food.order.system.domain.entity.*;
import com.food.order.system.domain.event.OrderCancelledEvent;
import com.food.order.system.domain.event.OrderCreatedEvent;
import com.food.order.system.domain.event.OrderPaidEvent;
import com.food.order.system.domain.valueobject.StreetAddress;
import com.food.order.system.dto.create.CreateOrderCommand;
import com.food.order.system.dto.create.CreateOrderResponse;
import com.food.order.system.dto.create.OrderAddress;
import com.food.order.system.dto.message.CustomerModel;
import com.food.order.system.dto.track.TrackOrderResponse;
import com.food.order.system.outbox.model.approval.OrderApprovalEventPayload;
import com.food.order.system.outbox.model.approval.OrderApprovalProduct;
import com.food.order.system.outbox.model.payment.OrderPaymentEventPayload;
import com.food.order.system.valueobject.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {

    public Customer customerModelToCustomer(CustomerModel customerModel) {
        return new Customer(new CustomerId(UUID.fromString(customerModel.getId())),
                customerModel.getUsername(),
                customerModel.getFirstName(),
                customerModel.getLastName());
    }


    public OrderPaymentEventPayload orderCancelledEventToOrderPaymentEventPayload(
            OrderCancelledEvent orderCancelledEvent) {
        return OrderPaymentEventPayload.builder()
                .orderId(orderCancelledEvent.getOrder().getId().getValue().toString())
                .paymentOrderStatus(PaymentOrderStatus.CANCELLED.name())
                .customerId(orderCancelledEvent.getOrder().getCustomerId().getValue().toString())
                .price(orderCancelledEvent.getOrder().getPrice().getAmount())
                .createdAt(orderCancelledEvent.getCreatedAt())
                .build();
    }

    public OrderApprovalEventPayload orderPaidEventToOrderApprovalEventPayload(OrderPaidEvent orderPaidEvent) {
        return OrderApprovalEventPayload.builder()
                .orderId(orderPaidEvent.getOrder().getId().getValue().toString())
                .restaurantId(orderPaidEvent.getOrder().getRestaurantId().getValue().toString())
                .restaurantOrderStatus(RestaurantOrderStatus.PAID.name())
                .products(orderPaidEvent.getOrder().getItems().stream()
                        .map(orderItem -> OrderApprovalProduct.builder()
                                .id(orderItem.getProduct().getId().getValue().toString())
                                .quantity(orderItem.getQuantity())
                                .build())
                        .toList())
                .price(orderPaidEvent.getOrder().getPrice().getAmount())
                .createdAt(orderPaidEvent.getCreatedAt())
                .build();
    }

    public OrderPaymentEventPayload orderCreatedEventToOrderPaymentEventPayload(OrderCreatedEvent order) {
        return OrderPaymentEventPayload.builder()
                .orderId(order.getOrder().getId().getValue().toString())
                .customerId(order.getOrder().getCustomerId().getValue().toString())
                .price(order.getOrder().getPrice().getAmount())
                .createdAt(order.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .build();
    }

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

    private List<OrderItem> orderItemsToOrderItemEntities(List<com.food.order.system.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem ->
                    OrderItem.builder()
                            .product(new Product(new ProductId(orderItem.productId())))
                            .price(new Money(orderItem.price()))
                            .quantity(orderItem.quantity())
                            .subTotal(new Money(orderItem.subTotal()))
                            .build())
        .toList();
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
