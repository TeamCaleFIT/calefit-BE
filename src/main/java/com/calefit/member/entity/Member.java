package com.calefit.member.entity;

import com.calefit.common.domain.BaseTime;
import com.calefit.crew.entity.Crew;
import com.calefit.member.domain.BodyInfo;
import com.calefit.member.domain.Email;
import com.calefit.member.domain.NickName;
import com.calefit.member.domain.Password;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private NickName nickName;

    @Embedded
    private Password password;

    @Embedded
    private BodyInfo bodyInfo;

    private boolean isJoinedCrew;
    private String role;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

}
