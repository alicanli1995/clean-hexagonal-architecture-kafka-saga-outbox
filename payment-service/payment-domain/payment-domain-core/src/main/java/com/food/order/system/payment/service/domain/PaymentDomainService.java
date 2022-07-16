package com.food.order.system.payment.service.domain;

import com.food.order.system.payment.service.domain.entity.CreditEntry;
import com.food.order.system.payment.service.domain.entity.CreditHistory;
import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.order.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.order.system.payment.service.domain.event.PaymentEvent;
import com.food.order.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.order.system.event.publisher.DomainEventPublisher;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvent validateAndInitializePayment(  Payment paymentEvent,
                                                CreditEntry creditEntry,
                                                List<CreditHistory> creditHistory,
                                                List<String> failureMessages,
                                                DomainEventPublisher<PaymentCompletedEvent> publisher,
                                                DomainEventPublisher<PaymentFailedEvent> failedEventDomainEventPublisher);

    PaymentEvent validateAndCancelledPayment(  Payment paymentEvent,
                                                CreditEntry creditEntry,
                                                List<CreditHistory> creditHistory,
                                                List<String> failureMessages,
                                                DomainEventPublisher<PaymentCancelledEvent> publisher,
                                                DomainEventPublisher<PaymentFailedEvent> failedEventDomainEventPublisher);
}
