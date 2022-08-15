package com.calefit.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class BodyInfo {

    private String height;
    private String body_weight;
}
