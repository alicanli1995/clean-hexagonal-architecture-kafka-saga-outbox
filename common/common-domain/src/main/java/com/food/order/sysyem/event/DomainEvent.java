package com.food.order.sysyem.event;


// Base Domain Event Generic Class
public interface DomainEvent<T> {

    void fire();

}
