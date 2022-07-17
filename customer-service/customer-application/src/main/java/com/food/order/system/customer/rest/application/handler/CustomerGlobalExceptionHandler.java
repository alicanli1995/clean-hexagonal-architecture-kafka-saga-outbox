package com.food.order.system.customer.rest.application.handler;

import com.food.order.system.application.handler.ErrorDTO;
import com.food.order.system.application.handler.GlobalExceptionHandler;
import com.food.order.system.customer.domain.exception.CustomerDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CustomerGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {CustomerDomainException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(CustomerDomainException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage()).build();
    }

}
