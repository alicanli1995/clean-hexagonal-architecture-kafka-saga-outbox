package com.food.order.system.messaging.mapper;

import com.food.order.system.domain.event.OrderCancelledEvent;
import com.food.order.system.domain.event.OrderCreatedEvent;
import com.food.order.system.domain.event.OrderPaidEvent;
import com.food.order.system.kafka.order.avro.model.*;
import com.food.order.system.dto.message.PaymentResponse;
import com.food.order.system.dto.message.RestaurantApprovalResponse;
import com.food.order.system.outbox.model.approval.OrderApprovalEventPayload;
import com.food.order.system.outbox.model.payment.OrderPaymentEventPayload;
import com.food.order.system.valueobject.OrderApprovalStatus;
import com.food.order.system.valueobject.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        var order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {
        var order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setOrderId(order.getId().getValue().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setId(UUID.randomUUID().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }


    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent event) {
        var order = event.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
                .setProducts(order.getItems().stream()
                        .map(item -> Product.newBuilder()
                                .setId(item.getProduct().getId().getValue().toString())
                                .setQuantity(item.getQuantity())
                                .build())
                        .toList())
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(event.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel message) {
        return PaymentResponse.builder()
                .orderId(message.getOrderId())
                .sagaId(message.getSagaId())
                .paymentId(message.getPaymentId())
                .customerId(message.getCustomerId())
                .orderId(message.getOrderId())
                .price(message.getPrice())
                .createdAt(message.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(message.getPaymentStatus().name()))
                .failureMessages(message.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(RestaurantApprovalResponseAvroModel message) {
        return RestaurantApprovalResponse.builder()
                .orderId(message.getOrderId())
                .sagaId(message.getSagaId())
                .restaurantId(message.getRestaurantId())
                .id(message.getId())
                .sagaId(message.getSagaId())
                .createdAt(message.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(message.getOrderApprovalStatus().name()))
                .failureMessages(message.getFailureMessages())
                .build();
    }

    public RestaurantApprovalRequestAvroModel
    orderApprovalEventToRestaurantApprovalRequestAvroModel(String sagaId, OrderApprovalEventPayload
            orderApprovalEventPayload) {
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(orderApprovalEventPayload.getOrderId())
                .setRestaurantId(orderApprovalEventPayload.getRestaurantId())
                .setRestaurantOrderStatus(RestaurantOrderStatus
                        .valueOf(orderApprovalEventPayload.getRestaurantOrderStatus()))
                .setProducts(orderApprovalEventPayload.getProducts().stream().map(orderApprovalEventProduct ->
                        Product.newBuilder()
                                .setId(orderApprovalEventProduct.getId())
                                .setQuantity(orderApprovalEventProduct.getQuantity())
                                .build()).toList())
                .setPrice(orderApprovalEventPayload.getPrice())
                .setCreatedAt(orderApprovalEventPayload.getCreatedAt().toInstant())
                .build();
    }

    public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(String sagaId, OrderPaymentEventPayload orderPaymentEventPayload) {
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setCustomerId(orderPaymentEventPayload.getCustomerId())
                .setOrderId(orderPaymentEventPayload.getOrderId())
                .setPrice(orderPaymentEventPayload.getPrice())
                .setCreatedAt(orderPaymentEventPayload.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(orderPaymentEventPayload.getPaymentOrderStatus()))
                .build();


    }
}
