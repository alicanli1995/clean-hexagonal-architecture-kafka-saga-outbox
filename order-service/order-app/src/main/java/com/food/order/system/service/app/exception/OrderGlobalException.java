package com.food.order.system.service.app.exception;

import com.food.order.system.application.handler.ErrorDTO;
import com.food.order.system.application.handler.GlobalExceptionHandler;
import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.domain.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class OrderGlobalException extends GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {OrderDomainException.class})
    @ResponseBody
    public ErrorDTO handleOrderDomainException(OrderDomainException e) {
        log.error("Error occurred: {}", e.getMessage());
        return ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {OrderNotFoundException.class})
    @ResponseBody
    public ErrorDTO handleOrderDomainException(OrderNotFoundException e) {
        log.error("Error occurred: {}", e.getMessage());
        return ErrorDTO.builder()
                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }



}
