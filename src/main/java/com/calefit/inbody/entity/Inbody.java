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

    private LocalDateTime date;

    @Embedded
    private BodyComposition bodyComposition;

    public Inbody(Member member, LocalDateTime date,
        BodyComposition bodyComposition) {
        this.member = member;
        this.date = date;
        this.bodyComposition = bodyComposition;
    }
}
