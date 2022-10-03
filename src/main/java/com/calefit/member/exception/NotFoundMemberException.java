package com.calefit.member.exception;

import com.calefit.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends BusinessException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
