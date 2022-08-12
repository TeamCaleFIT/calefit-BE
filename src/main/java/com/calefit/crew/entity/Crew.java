package com.calefit.crew.entity;

import com.calefit.common.domain.BaseTime;
import com.calefit.crew.domain.Description;
import com.calefit.crew.domain.Image;
import com.calefit.crew.domain.Name;
import com.calefit.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Crew extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Description description;

    @Embedded
    private Image image;

    @OneToMany(mappedBy = "member")
    private List<Member> members = new ArrayList<>();

    private Long member_cnt;
    private Long score;

}
