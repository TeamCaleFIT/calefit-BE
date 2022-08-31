package com.calefit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ExceptionCodes exceptionCode = ExceptionCodes.findByExceptionClass(getClass());

    public BusinessException() {
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public abstract HttpStatus getHttpStatus();
}
