package com.calefit.exception;

import com.calefit.common.base.CodeAndMessages;
import com.calefit.member.exception.NotFoundMemberException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ExceptionCodes implements CodeAndMessages {
    //Member
    MEMBER_NOT_FOUND("MNF", "멤버 정보가 존재하지 않습니다.", NotFoundMemberException.class);

    private final String code;
    private final String message;
    private final Class<? extends Exception> type;

    ExceptionCodes(String code, String message, Class<? extends Exception> type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public static ExceptionCodes findByExceptionClass(Class<? extends Exception> type) {
        return Arrays.stream(values())
                .filter(code -> code.type.equals(type))
                .findAny()
                .orElseThrow(NotFoundErrorCodeException::new);
    }
}
