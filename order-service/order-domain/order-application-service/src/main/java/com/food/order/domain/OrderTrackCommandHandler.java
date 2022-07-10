package com.food.order.domain;

import com.food.order.domain.dto.track.TrackOrderQuery;
import com.food.order.domain.dto.track.TrackOrderResponse;
import com.food.order.domain.mapper.OrderDataMapper;
import com.food.order.domain.ports.output.repository.OrderRepository;
import com.food.order.system.domain.exception.OrderNotFoundException;
import com.food.order.system.domain.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        var order =  orderRepository.findByTrackingId
                (new TrackingId(trackOrderQuery.orderTrackingId()))
                .orElseThrow(() -> new OrderNotFoundException
                        ("Order not found with tracking id : +  " + trackOrderQuery.orderTrackingId()));
        return orderDataMapper.orderToTrackOrderResponse(order);
    }
}
