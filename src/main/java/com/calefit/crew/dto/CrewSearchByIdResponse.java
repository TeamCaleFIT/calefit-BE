package com.calefit.crew.dto;

import com.calefit.crew.entity.Crew;
import com.calefit.member.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class CrewSearchByIdResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String image;
    private final Long memberCount;
    private final Long score;
    private final List<Member> members;

    public CrewSearchByIdResponse(Long id, String name, String description, String image, Long memberCount, Long score, List<Member> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.memberCount = memberCount;
        this.score = score;
        this.members = members;
    }

    public static CrewSearchByIdResponse from(Crew crew) {
        return new CrewSearchByIdResponse(
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
