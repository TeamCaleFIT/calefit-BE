package com.calefit.exception;

import org.springframework.http.HttpStatus;

public class NotFoundErrorCodeException extends BusinessException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
