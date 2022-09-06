package com.calefit.inbody.domain;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class BodyComposition {

    private Double muscle;
    private Double bodyFat;
    private Double bodyWeight;

    public BodyComposition(Double muscle, Double bodyFat, Double bodyWeight) {
        this.muscle = muscle;
        this.bodyFat = bodyFat;
        this.bodyWeight = bodyWeight;
    }

    public void changeMuscle(Double muscle) {
        this.muscle = muscle;
    }

    public void changeBodyFat(Double bodyFat) {
        this.bodyFat = bodyFat;
    }

    public void changeBodyWeight(Double bodyWeight) {
        this.bodyWeight = bodyWeight;
    }
}
