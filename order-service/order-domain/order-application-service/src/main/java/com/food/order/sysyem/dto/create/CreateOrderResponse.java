package com.food.order.sysyem.dto.create;

import com.food.order.sysyem.valueobject.OrderStatus;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
public record CreateOrderResponse(@NotNull UUID orderTrackingId,
                                  @NotNull OrderStatus orderStatus,
                                  @NotNull String message) {
}
