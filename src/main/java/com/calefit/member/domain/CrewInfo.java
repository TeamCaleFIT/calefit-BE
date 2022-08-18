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
        this.isCrewJoined = isCrewJoined;
        this.role = role;
    }
}
