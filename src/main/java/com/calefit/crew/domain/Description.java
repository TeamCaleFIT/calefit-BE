package com.calefit.crew.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class Description {

    private String description;
}
