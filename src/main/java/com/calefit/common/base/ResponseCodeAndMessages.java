package com.calefit.common.base;

public enum ResponseCodeAndMessages implements CodeAndMessages {

    //Crew
    CREW_MODIFY_SUCCESS("CMS","수정이 완료되었습니다."),

    //Inbody
    INBODY_CREATE_SUCCESS("ICS", "인바디를 등록하였습니다.");

    private final String code;
    private final String message;

    ResponseCodeAndMessages(String code, String message) {
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
