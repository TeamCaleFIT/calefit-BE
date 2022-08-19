package com.calefit.crew.entity;

import com.calefit.common.domain.BaseTime;
import com.calefit.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
public class Crew extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    private Integer memberCount;
    private Long score;

    @OneToMany(mappedBy = "crew")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "crew")
    private List<CrewApplication> crewApplications = new ArrayList<>();

    public Crew(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
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
}
