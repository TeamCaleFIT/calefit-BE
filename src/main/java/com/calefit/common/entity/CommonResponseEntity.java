package com.calefit.common.entity;

import com.calefit.common.base.BaseResponse;
import com.calefit.common.base.CodeAndMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class CommonResponseEntity<T> extends ResponseEntity<BaseResponse<T>> {

    public CommonResponseEntity(CodeAndMessage codeAndMessages, HttpStatus httpStatus) {
        this(codeAndMessages, null, httpStatus);
    }

    public CommonResponseEntity(CodeAndMessage codeAndMessages, T data, HttpStatus httpStatus) {
        this(codeAndMessages, data, null, httpStatus);
    }

    public CommonResponseEntity(CodeAndMessage codeAndMessages, T data, MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        super(new BaseResponse<>(codeAndMessages.getCode(), codeAndMessages.getMessage(), data), headers, httpStatus);
    }
}
