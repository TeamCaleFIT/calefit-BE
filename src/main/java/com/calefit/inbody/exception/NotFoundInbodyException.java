package com.calefit.inbody.exception;

import com.calefit.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundInbodyException extends BusinessException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
