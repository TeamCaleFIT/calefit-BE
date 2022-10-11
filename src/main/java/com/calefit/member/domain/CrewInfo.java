package com.calefit.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class CrewInfo {

    private boolean isCrewJoined;
    private String role;

    public CrewInfo(boolean isCrewJoined, String role) {
        //TODO null 검증 로직 필요
        this.isCrewJoined = isCrewJoined;
        this.role = role;
    }
}
