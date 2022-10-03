package com.calefit.member.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class MemberLoginRequest {

    @NotBlank(message = "필수 입력값입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "필수 입력값입니다.")
    @Size(min = 8, max = 16, message = "8~16글자 이내로(영문기준) 입력해주세요.")
    private String password;
}
