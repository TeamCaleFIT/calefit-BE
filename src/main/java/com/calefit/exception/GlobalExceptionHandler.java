package com.calefit.exception;

import com.calefit.exception.Entity.CommonExceptionResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public CommonExceptionResponseEntity handleBusinessException(BusinessException ex) {
        return new CommonExceptionResponseEntity(ex.getExceptionCode(), ex.getHttpStatus());
    }
}
