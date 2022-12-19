package com.calefit.member.entity;

import com.calefit.auth.domain.provider.ProviderType;
import com.calefit.common.base.BaseTime;
import com.calefit.crew.entity.Crew;
import com.calefit.inbody.entity.Inbody;
import com.calefit.member.domain.CrewInfo;
import com.calefit.template.entity.Template;
import com.calefit.workout.entity.Schedule;
import com.sun.istack.NotNull;
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
    private String memberId;
    private String email;
    private String nickname;
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProviderType providerType;

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

    public Member(String memberId,
                  String email,
                  String nickname,
                  String profileUrl,
                  ProviderType providerType) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.providerType = providerType;
        this.crewInfo = new CrewInfo(false, null);
    }

    public void addCrew(Crew crew, String role) {
        this.crew = crew;
        crew.getMembers().add(this);
        this.crewInfo = new CrewInfo(true, role);
    }

    public Member update(String email, String nickname, String profileUrl) {
        this.email = email;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        return this;
    }

    public void removeCaptainAuthority() {
        this.crewInfo = new CrewInfo(false, null);
    }

//    public boolean isPasswordMatched(String requestPassword) {
//        return password.validatePassword(requestPassword);
//    }
}
