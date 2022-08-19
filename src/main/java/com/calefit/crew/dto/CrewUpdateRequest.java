package com.calefit.crew.dto;

import lombok.Getter;

@Getter
public class CrewUpdateRequest {

    private Long memberId;
    private String name;
    private String description;
    private String image;
}