package com.food.order.system.ports.input.service;

import com.food.order.system.dto.create.CreateOrderCommand;
import com.food.order.system.dto.create.CreateOrderResponse;
import com.food.order.system.dto.track.TrackOrderQuery;
import com.food.order.system.dto.track.TrackOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);
    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);

}
