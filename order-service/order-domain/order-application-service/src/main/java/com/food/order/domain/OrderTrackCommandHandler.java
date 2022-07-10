package com.food.order.domain;

import com.food.order.domain.dto.track.TrackOrderQuery;
import com.food.order.domain.dto.track.TrackOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderTrackCommandHandler {

    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        log.info("trackOrder: {}", trackOrderQuery);
        return null;
    }
}
