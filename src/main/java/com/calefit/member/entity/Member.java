package com.calefit.member.entity;

import com.calefit.common.base.BaseTime;
import com.calefit.crew.entity.Crew;
import com.calefit.inbody.entity.Inbody;
import com.calefit.member.domain.CrewInfo;
import com.calefit.template.entity.Template;
import com.calefit.workout.entity.Schedule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nickname;
    private String password;

    @Embedded
    private CrewInfo crewInfo;

    @ManyToOne(fetch = LAZY)
    private Crew crew;

    @OneToMany(mappedBy = "member")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Template> templates = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Inbody> inbodies = new ArrayList<>();

    public Member(String email,
                  String nickname,
                  String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.crewInfo = new CrewInfo(false, null);
    }

    public void addCrew(Crew crew, String role) {
        this.crew = crew;
        crew.getMembers().add(this);
        this.crewInfo = new CrewInfo(true, role);
    }

    public void removeCaptainAuthority() {
        this.crewInfo = new CrewInfo(false, null);
    }
}
