package com.food.order.system.outbox;

public interface OutboxScheduler {
    void processOutboxMessage();
}
