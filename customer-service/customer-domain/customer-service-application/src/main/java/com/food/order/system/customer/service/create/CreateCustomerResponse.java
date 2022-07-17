package com.food.order.system.customer.service.create;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
public record CreateCustomerResponse(@NotNull UUID customerId, @NotNull String message) {
}
