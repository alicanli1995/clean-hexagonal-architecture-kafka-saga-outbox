package com.food.order.sysyem.ports.input.service;

import com.food.order.sysyem.dto.create.CreateOrderCommand;
import com.food.order.sysyem.dto.create.CreateOrderResponse;
import com.food.order.sysyem.dto.track.TrackOrderQuery;
import com.food.order.sysyem.dto.track.TrackOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);
    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);

}
