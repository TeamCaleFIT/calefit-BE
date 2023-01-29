package com.calefit.common.base;

public enum ResponseCodes implements CodeAndMessages {

    //Crew
    CREW_MODIFY_SUCCESS("CMOS","수정이 완료되었습니다."),

    //Member
    MEMBER_SEARCH_SUCCESS("MSES", "멤버 프로필 조회가 성공하였습니다."),
    MEMBER_SIGNUP_SUCCESS("MSIS","멤버 회원가입이 성공하였습니다." ),
    MEMBER_LOGIN_SUCCESS("MLOS","로그인이 성공하였습니다." ),

    //Inbody
    INBODY_CREATE_SUCCESS("ICRS", "인바디를 등록하였습니다."),
    INBODY_SEARCH_SUCCESS("ISES", "인바디를 조회하였습니다."),
    INBODY_DELETE_SUCCESS("IDES", "인바디를 삭제히였습니다."),
    INBODY_UPDATE_SUCCESS("IUPS", "인바디를 수정하였습니다."),

    //OAuth
    OAUTH_LOGIN_SUCCESS("ALOS", "로그인이 성공하였습니다."),

    //Workout
    WORKOUT_SEARCH_SUCCESS("WSES", "운동 종목 조회에 성공하였습니다."),
    WORKOUT_LOG_CREATE_SUCCESS("WCRS", "운동 일지 등록에 성공하였습니다.");

    private final String code;
    private final String message;

    ResponseCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
