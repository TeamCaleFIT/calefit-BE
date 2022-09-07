package com.calefit.crew.dto;

import com.calefit.crew.entity.Crew;
import com.calefit.member.member.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class CrewDetailedResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String image;
    private final Integer memberCount;
    private final Long score;
    private final List<Member> members;

    public CrewDetailedResponse(Long id, String name, String description, String image, Integer memberCount, Long score, List<Member> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.memberCount = memberCount;
        this.score = score;
        this.members = members;
    }

    public static CrewDetailedResponse from(Crew crew) {
        return new CrewDetailedResponse(
                crew.getId(),
                crew.getName(),
                crew.getDescription(),
                crew.getImage(),
                crew.getMemberCount(),
                crew.getScore(),
                crew.getMembers()
        );
    }
}
