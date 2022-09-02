package com.calefit.exception;

import com.calefit.common.base.CodeAndMessages;
import com.calefit.member.exception.NotAvailableMemberEmailException;
import com.calefit.member.exception.NotAvailableMemberNicknameException;
import com.calefit.member.exception.NotFoundMemberException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ExceptionCodes implements CodeAndMessages {
    //Member
    NOT_FOUND_MEMBER ("NFOM", "멤버 정보가 존재하지 않습니다.", NotFoundMemberException.class),
    NOT_AVAILABLE_MEMBER_EMAIL ("NAME", "동일한 이메일이 존재합니다.", NotAvailableMemberEmailException.class),
    NOT_AVAILABLE_MEMBER_NICKNAME ("NAMN", "동일한 닉네임이 존재합니다.", NotAvailableMemberNicknameException.class);

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
