package com.food.order.system.payment.application.service;

import com.food.order.system.payment.application.service.dto.PaymentRequest;
import com.food.order.system.payment.application.service.exception.PaymentApplicationServiceException;
import com.food.order.system.payment.application.service.mapper.PaymentDataMapper;
import com.food.order.system.payment.application.service.ports.output.repository.CreditEntryRepository;
import com.food.order.system.payment.application.service.ports.output.repository.CreditHistoryRepository;
import com.food.order.system.payment.application.service.ports.output.repository.PaymentRepository;
import com.food.order.system.payment.service.domain.PaymentDomainService;
import com.food.order.system.payment.service.domain.entity.CreditEntry;
import com.food.order.system.payment.service.domain.entity.CreditHistory;
import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.order.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.order.system.payment.service.domain.event.PaymentEvent;
import com.food.order.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;
import com.food.order.sysyem.valueobject.CustomerId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestHelper {
    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    private final DomainEventPublisher<PaymentCompletedEvent> publisher;
    private final DomainEventPublisher<PaymentCancelledEvent> publisherCancelled;

    private final DomainEventPublisher<PaymentFailedEvent> failedEventDomainEventPublisher;

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment complete event for id : {}", paymentRequest.getOrderId());
        var payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        var creditEntry = getCreditEntry(payment.getCustomerId());
        var creditHistory = getCreditHistory(payment.getCustomerId());
        List<String> failureMessage = new ArrayList<>();

        var paymentEvent = paymentDomainService.validateAndInitializePayment
                (payment, creditEntry, creditHistory, failureMessage,publisher,failedEventDomainEventPublisher);

        persistDbObject(payment, creditEntry, creditHistory, failureMessage);
        return paymentEvent;
    }


    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received payment cancel event for id : {}", paymentRequest.getOrderId());
        var payment = paymentRepository.findByOrderId
                (UUID.fromString(paymentRequest.getOrderId())).orElseThrow(
                () -> new PaymentApplicationServiceException("Payment not found"));
        var creditEntry = getCreditEntry(payment.getCustomerId());
        var creditHistory = getCreditHistory(payment.getCustomerId());
        List<String> failureMessage = new ArrayList<>();
        var paymentEvent = paymentDomainService.validateAndCancelledPayment
                (payment, creditEntry, creditHistory, failureMessage,publisherCancelled,failedEventDomainEventPublisher);

        persistDbObject(payment, creditEntry, creditHistory, failureMessage);
        return paymentEvent;
    }

    private void persistDbObject(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistory, List<String> failureMessage) {
        paymentRepository.save(payment);
        if (failureMessage.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistory.get(creditHistory.size() - 1));
        }
    }
    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        return creditHistoryRepository.findByCustomerId(customerId).orElseThrow(
                () -> new PaymentApplicationServiceException("No credit history found for customer id : " + customerId));
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        return creditEntryRepository.findByCustomerId(customerId).orElseThrow(
                () -> new PaymentApplicationServiceException("Credit entry not found for customer id : " + customerId));
    }
}
