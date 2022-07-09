package com.food.order.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@Builder
public record OrderAddress(@NotNull @Max(value = 50) String street,
                           @NotNull @Max(value = 50) String city,
                           @NotNull @Max(value = 10) String postalCode) {
}
