package com.calefit.inbody.dto;

import com.calefit.inbody.entity.Inbody;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SearchInbodyResponse {

    private Long id;
    private LocalDateTime measuredDateTime;
    private Double muscle;
    private Double bodyFat;
    private Double bodyWeight;

    public SearchInbodyResponse(Long id, LocalDateTime date, Double muscle, Double bodyFat,
        Double bodyWeight) {
        this.id = id;
        this.measuredDateTime = date;
        this.muscle = muscle;
        this.bodyFat = bodyFat;
        this.bodyWeight = bodyWeight;
    }

    public static SearchInbodyResponse from(Inbody inbody) {
        return new SearchInbodyResponse(
            inbody.getId(), inbody.getMeasuredDateTime(), inbody.getBodyComposition().getMuscle(),
            inbody.getBodyComposition().getBodyFat(), inbody.getBodyComposition().getBodyWeight()
        );
    }
}
