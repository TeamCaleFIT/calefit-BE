package com.calefit.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class BodyInfo {

    private Double height;
    private Double bodyWeight;

    public BodyInfo(Double height, Double bodyWeight) {
        this.height = height;
        this.bodyWeight = bodyWeight;
    }
}
