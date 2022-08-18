package com.calefit.member.entity;

import com.calefit.common.domain.BaseTime;
import com.calefit.crew.entity.Crew;
import com.calefit.member.domain.BodyInfo;
import com.calefit.template.entity.Template;
import com.calefit.workout.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private String email;

    @Embedded
    private String nickname;

    @Embedded
    private String password;

    @Embedded
    private BodyInfo bodyInfo;

    private boolean isCrewJoined;
    private String role;

    @ManyToOne(fetch = LAZY)
    private Crew crew;

    @OneToMany(mappedBy = "member")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Template> templates = new ArrayList<>();
}
