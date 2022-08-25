package com.calefit.member.member.exception;

import com.calefit.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotAvailableMemberEmailException extends BusinessException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}