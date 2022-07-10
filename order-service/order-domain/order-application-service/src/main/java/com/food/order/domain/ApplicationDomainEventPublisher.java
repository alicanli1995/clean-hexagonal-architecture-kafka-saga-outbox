package com.food.order.domain;

import com.food.order.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationDomainEventPublisher implements
        ApplicationContextAware,
        DomainEventPublisher<OrderCreatedEvent> {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void publish(OrderCreatedEvent event) {
        this.applicationEventPublisher.publishEvent(event);
        log.info("publish order created event with id : {}", event.getOrder().getId().getValue());
    }
}
