package com.calefit.common.base;

public enum ResponseCodes implements CodeAndMessages {

    //Crew
    CREW_MODIFY_SUCCESS("CMS","수정이 완료되었습니다."),

    //Member
    MEMBER_SEARCH_SUCCESS("MSS", "멤버 프로필 조회가 성공하였습니다.");

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
