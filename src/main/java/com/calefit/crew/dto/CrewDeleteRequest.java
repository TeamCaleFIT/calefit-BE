package com.calefit.crew.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CrewDeleteRequest {

    @NotNull(message = "요청자의 식별자는 필수 입니다.")
    private Long memberId;
}
