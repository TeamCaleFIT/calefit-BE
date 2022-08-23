package com.calefit.crew.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class CrewUpdateRequest {

    @NotBlank(message = "요청자의 식별자는 필수 입니다.")
    private Long memberId;

    @Size(max = 10)
    private String name;

    @Size(max = 30)
    private String description;

    @URL
    private String image;
}