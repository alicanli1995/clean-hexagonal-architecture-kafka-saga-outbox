package com.food.order.domain;

import com.food.order.domain.dto.create.CreateOrderCommand;
import com.food.order.domain.dto.create.OrderAddress;
import com.food.order.domain.dto.create.OrderItem;
import com.food.order.domain.mapper.OrderDataMapper;
import com.food.order.domain.ports.input.service.OrderApplicationService;
import com.food.order.domain.ports.output.repository.CustomerRepository;
import com.food.order.domain.ports.output.repository.OrderRepository;
import com.food.order.domain.ports.output.repository.RestaurantRepository;
import com.food.order.domain.valueobject.*;
import com.food.order.system.domain.entity.Customer;
import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.entity.Product;
import com.food.order.system.domain.entity.Restaurant;
import com.food.order.system.domain.exception.OrderDomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
class OrderApplicationTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.randomUUID();
    private final UUID RESTAURANT_ID = UUID.randomUUID();

    private final Money PRODUCT_PRICE = new Money(BigDecimal.valueOf(50));
    private final UUID PRODUCT_ID = UUID.randomUUID();
    private final UUID ORDER_ID = UUID.randomUUID();
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    public void init(){
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .orderAddress(OrderAddress.builder()
                        .street("street")
                        .city("city")
                        .postalCode("41780")
                        .build())
                .price(PRICE)
                .orderItems(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .quantity(1)
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .quantity(3)
                                .build()))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .orderAddress(OrderAddress.builder()
                        .street("street")
                        .city("city")
                        .postalCode("41780")
                        .build())
                .price(PRICE)
                .orderItems(List.of(OrderItem.builder()
                        .productId(PRODUCT_ID)
                        .price(new BigDecimal("50.00"))
                        .subTotal(new BigDecimal("100.00"))
                        .quantity(2)
                        .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .quantity(3)
                                .build()))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .orderAddress(OrderAddress.builder()
                        .street("street")
                        .city("city")
                        .postalCode("41780")
                        .build())
                .price(new BigDecimal("210.00"))
                .orderItems(List.of(OrderItem.builder()
                        .productId(PRODUCT_ID)
                        .price(new BigDecimal("60.00"))
                        .subTotal(new BigDecimal("60.00"))
                        .quantity(1)
                        .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .quantity(3)
                                .build()))
                .build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurant = Restaurant.builder()
                .id(new RestaurantId(RESTAURANT_ID))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "product-1", PRODUCT_PRICE),
                        new Product(new ProductId(UUID.randomUUID()), "product-2", new Money(BigDecimal.valueOf(50)))
                ))
                .isActive(Boolean.TRUE)
                .build();


        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        Mockito.when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        Mockito.when(restaurantRepository.findRestaurantInformation
                        (orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurant));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
    }


    @Test
    void testCreateOrder(){
        var response = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(OrderStatus.PENDING, response.orderStatus());
        assertEquals("Order created successfully", response.message());
        assertNotNull(response.orderTrackingId());
    }

    @Test
    void testCreateOrderWrongPrice(){
        OrderDomainException exception = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        assertEquals("Order total price is not equal to the sum of order items prices", exception.getMessage());
    }

    @Test
    void testCreateOrderWrongProductPrice(){
        OrderDomainException exception = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        assertEquals("Order item price is not valid", exception.getMessage());
    }

    @Test
    void testCreateOrderWithPassiveRestaurant(){
        Restaurant restaurant = Restaurant.builder()
                .id(new RestaurantId(RESTAURANT_ID))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "product-1", PRODUCT_PRICE),
                        new Product(new ProductId(UUID.randomUUID()), "product-2", new Money(BigDecimal.valueOf(50)))
                ))
                .isActive(Boolean.FALSE)
                .build();

        Mockito.when(restaurantRepository.findRestaurantInformation
                (orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurant));
        OrderDomainException exception = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommand));
        assertEquals("Restaurant is not active, please try again later. Restaurant id: "+ restaurant.getId(), exception.getMessage());
    }

}
