package com.calefit.auth.exception;

import com.calefit.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends BusinessException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
