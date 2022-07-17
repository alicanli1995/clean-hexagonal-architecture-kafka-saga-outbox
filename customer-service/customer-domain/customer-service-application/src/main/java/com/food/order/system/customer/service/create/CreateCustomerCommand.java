package com.food.order.system.customer.service.create;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
public record CreateCustomerCommand(@NotNull UUID customerId, @NotNull String username, @NotNull String firstName,
                                    @NotNull String lastName) {
}
