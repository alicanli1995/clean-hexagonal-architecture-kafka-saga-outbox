package com.food.order.sysyem.dto.track;

import com.food.order.sysyem.valueobject.OrderStatus;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Builder
public record TrackOrderResponse(@NotNull UUID orderTrackingId,
                                 @NotNull OrderStatus orderStatus,
                                 List<String> failureMessages) {
}
