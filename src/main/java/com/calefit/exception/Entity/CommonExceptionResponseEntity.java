package com.calefit.exception.Entity;

import com.calefit.common.base.CodeAndMessages;
import com.calefit.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommonExceptionResponseEntity extends ResponseEntity<ErrorResponse> {

    public CommonExceptionResponseEntity(CodeAndMessages codeAndMessages, HttpStatus httpStatus) {
        super(new ErrorResponse(codeAndMessages.getCode(), codeAndMessages.getMessage()),httpStatus);
    }
}
