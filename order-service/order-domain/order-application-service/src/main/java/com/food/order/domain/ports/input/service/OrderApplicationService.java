package com.food.order.domain.ports.input.service;

import com.food.order.domain.dto.create.CreateOrderCommand;
import com.food.order.domain.dto.create.CreateOrderResponse;
import com.food.order.domain.dto.track.TrackOrderQuery;
import com.food.order.domain.dto.track.TrackOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);
    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);

}
