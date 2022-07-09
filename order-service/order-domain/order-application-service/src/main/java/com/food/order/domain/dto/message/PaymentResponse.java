package com.food.order.domain.dto.message;

import com.food.order.domain.valueobject.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PaymentResponse {

    private String id;
    private String sagaId;
    private String orderId;
    private String paymentId;
    private String customerId;
    private BigDecimal price;
    private PaymentStatus paymentStatus;
    private List<String> failureMessages;
}
