package com.food.order.system.customer.service;


import com.food.order.system.customer.domain.CustomerDomainService;
import com.food.order.system.customer.domain.entity.Customer;
import com.food.order.system.customer.domain.event.CustomerCreatedEvent;
import com.food.order.system.customer.domain.exception.CustomerDomainException;
import com.food.order.system.customer.service.create.CreateCustomerCommand;
import com.food.order.system.customer.service.mapper.CustomerDataMapper;
import com.food.order.system.customer.service.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
class CustomerCreateCommandHandler {

    private final CustomerDomainService customerDomainService;

    private final CustomerRepository customerRepository;

    private final CustomerDataMapper customerDataMapper;



    @Transactional
    public CustomerCreatedEvent createCustomer(CreateCustomerCommand createCustomerCommand) {
        Customer customer = customerDataMapper.createCustomerCommandToCustomer(createCustomerCommand);
        CustomerCreatedEvent customerCreatedEvent = customerDomainService.validateAndInitiateCustomer(customer);
        Customer savedCustomer = customerRepository.createCustomer(customer);
        if (savedCustomer == null) {
            log.error("Could not save customer with id: {}", createCustomerCommand.customerId());
            throw new CustomerDomainException("Could not save customer with id " +
                    createCustomerCommand.customerId());
        }
        log.info("Returning CustomerCreatedEvent for customer id: {}", createCustomerCommand.customerId());
        return customerCreatedEvent;
    }
}
