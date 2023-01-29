package com.calefit.exception;

import com.calefit.auth.exception.ExpiredRefreshTokenException;
import com.calefit.auth.exception.InvalidRefreshTokenException;
import com.calefit.auth.exception.InvalidTokenException;
import com.calefit.auth.exception.NotFoundTokenException;
import com.calefit.common.base.CodeAndMessages;
import com.calefit.inbody.exception.NotFoundInbodyException;
import com.calefit.member.exception.*;
import com.calefit.workout.exception.NotFoundWorkoutException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ExceptionCodes implements CodeAndMessages {
    //Member
    NOT_FOUND_MEMBER("NFOM", "멤버 정보가 존재하지 않습니다.", NotFoundMemberException.class),
    NOT_FOUND_ALGORITHM("NFAL", "현재 환경에서 암호화 처리기능을 사용할 수 없습니다.", NotFoundAlgorithmException.class),
    NOT_AVAILABLE_MEMBER_LOGIN("NAML", "로그인이 실패하였습니다.", NotAvailableMemberLoginException.class),

    //Inbody
    NOT_FOUND_INBODY("NFOI", "인바디 정보가 존재하지 않습니다.", NotFoundInbodyException.class),

    //Oauth
    NOT_FOUND_TOKEN("NFOT", "토큰 정보가 존재하지 않습니다.", NotFoundTokenException.class),
    EXPIRED_REFRESH_TOKEN("ERET", "로그인 정보가 만료되었습니다.", ExpiredRefreshTokenException.class),
    INVALID_TOKEN("INTO", "토큰 타입이 유효하지 않습니다.", InvalidTokenException.class),
    INVALID_REFRESH_TOKEN("IRET", "로그인 정보가 일치하지 않습니다. 다시 로그인해주세요.", InvalidRefreshTokenException.class),

    //Workout
    NOT_FOUND_WORKOUT("NFWO", "운동 종목 정보가 존재하지 않습니다.", NotFoundWorkoutException.class);

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
