package com.food.order.system.data.access.restaurant.adapter;

import com.food.order.system.data.access.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.order.system.data.access.restaurant.repository.OrderApprovalJpaRepository;
import com.food.order.system.restaurant.domain.core.entity.OrderApproval;
import com.food.order.system.restaurant.domain.service.ports.output.repository.OrderApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;


    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return restaurantDataAccessMapper
                .orderApprovalEntityToOrderApproval(orderApprovalJpaRepository
                        .save(restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
    }

}
