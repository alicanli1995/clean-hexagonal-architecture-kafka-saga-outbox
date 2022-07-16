package com.food.order.system.service.app.api;

import com.food.order.system.dto.create.CreateOrderCommand;
import com.food.order.system.dto.create.CreateOrderResponse;
import com.food.order.system.dto.track.TrackOrderQuery;
import com.food.order.system.dto.track.TrackOrderResponse;
import com.food.order.system.ports.input.service.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestScope
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(value = "/api/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
        log.info("Creating order with command: {}", createOrderCommand);
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("Created order with tracking id: {}", createOrderResponse.orderTrackingId());
        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/{orderTrackingId}")
    public ResponseEntity<TrackOrderResponse> trackOrder(@PathVariable("orderTrackingId") UUID orderTrackingId) {
        TrackOrderResponse trackOrderResponse = orderApplicationService.trackOrder
                (new TrackOrderQuery(orderTrackingId));
        log.info("Tracked order with tracking id: {}", orderTrackingId);
        return ResponseEntity.ok(trackOrderResponse);
    }
}
