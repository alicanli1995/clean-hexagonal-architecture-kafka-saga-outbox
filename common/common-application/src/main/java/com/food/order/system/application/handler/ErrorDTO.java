package com.food.order.system.application.handler;

import lombok.Builder;

@Builder
public record ErrorDTO(String code, String message) {
}
