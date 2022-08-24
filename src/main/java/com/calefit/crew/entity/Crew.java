package com.calefit.crew.entity;

import com.calefit.common.domain.BaseTime;
import com.calefit.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Crew extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean deleted;
    private String name;
    private String description;
    private String image;
    private Integer memberCount;
    private long score;

    @OneToMany(mappedBy = "crew")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "crew")
    private List<CrewApplication> crewApplications = new ArrayList<>();

    public Crew(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.memberCount = 1;
    }

    public Crew(Long id,
                boolean deleted,
                String name,
                String description,
                String image,
                Integer memberCount,
                long score) {
        this.id = id;
        this.deleted = deleted;
        this.name = name;
        this.description = description;
        this.image = image;
        this.memberCount = memberCount;
        this.score = score;
    }

    public void updateCrew(String name, String description, String image) {
        //TODO 글자 길이 검증 로직 필요
        //TODO 커스텀 Exception 추가 필요
        if(!Objects.isNull(name)) {
            this.name = name;
        }
        if(!Objects.isNull(description)) {
            this.description = description;
        }
        if(!Objects.isNull(image)) {
            this.image = image;
        }
    }

    public void delete() {
        this.deleted = true;
    }
}
