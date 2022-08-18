package com.calefit.crew.entity;

import com.calefit.common.domain.BaseTime;
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
    private String name;
    private String description;
    private String image;
    private Long memberCount;
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
}
