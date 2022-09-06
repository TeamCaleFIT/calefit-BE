package com.calefit.inbody.entity;

import static javax.persistence.FetchType.*;

import com.calefit.inbody.domain.BodyComposition;
import com.calefit.member.member.entity.Member;
import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inbody {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Member member;

    private LocalDateTime measuredDateTime;

    @Embedded
    private BodyComposition bodyComposition;

    public Inbody(Member member, LocalDateTime measuredDateTime,
        BodyComposition bodyComposition) {
        this.member = member;
        this.measuredDateTime = measuredDateTime;
        this.bodyComposition = bodyComposition;
    }

    public void changeInbody(BodyComposition updateBodyComposition) {
        if (updateBodyComposition.getMuscle() != null) {
            bodyComposition.changeMuscle(updateBodyComposition.getMuscle());
        }
        if (updateBodyComposition.getBodyFat() != null) {
            bodyComposition.changeBodyFat(updateBodyComposition.getBodyFat());
        }
        if (updateBodyComposition.getBodyWeight() != null) {
            bodyComposition.changeBodyWeight(updateBodyComposition.getBodyWeight());
        }
    }
}
