package com.calefit.crew.entity;

import com.calefit.common.base.BaseTime;
import com.calefit.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
public class CrewApplication extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    private Member member;

    @ManyToOne(fetch = LAZY)
    private Crew crew;
}
