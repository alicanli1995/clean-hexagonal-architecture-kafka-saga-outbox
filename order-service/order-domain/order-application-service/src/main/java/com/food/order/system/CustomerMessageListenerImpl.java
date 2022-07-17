package com.food.order.system;

import com.food.order.system.dto.message.CustomerModel;
import com.food.order.system.mapper.OrderDataMapper;
import com.food.order.system.ports.input.message.listener.customer.CustomerMessageListener;
import com.food.order.system.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerMessageListenerImpl implements CustomerMessageListener {

    private final CustomerRepository customerRepository;
    private final OrderDataMapper orderDataMapper;


    @Override
    public void customerCreated(CustomerModel customerModel) {
        var customer = customerRepository.save
                (orderDataMapper.customerModelToCustomer(customerModel));

        if (Objects.isNull(customer)) {
            log.error("Customer not created");
        } else {
            log.info("Customer created");
        }



    }

}
