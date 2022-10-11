package com.calefit.member.exception;

import com.calefit.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundAlgorithmException extends BusinessException {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
