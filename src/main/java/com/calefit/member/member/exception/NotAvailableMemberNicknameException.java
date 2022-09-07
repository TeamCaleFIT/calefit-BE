package com.calefit.member.member.exception;

import com.calefit.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotAvailableMemberNicknameException extends BusinessException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
