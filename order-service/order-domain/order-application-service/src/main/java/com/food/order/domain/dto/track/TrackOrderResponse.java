package com.food.order.domain.dto.track;

import com.food.order.domain.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Builder
public record TrackOrderResponse(@NotNull UUID orderTrackingId,
                                 @NotNull OrderStatus orderStatus,
                                 List<String> failureMessages) {
}
