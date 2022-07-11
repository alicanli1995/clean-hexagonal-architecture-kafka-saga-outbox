package com.food.order.domain.dto.track;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
public record TrackOrderQuery(@NotNull UUID orderTrackingId) {
}
