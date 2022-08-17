package com.calefit.crew.dto;

import com.calefit.crew.entity.Crew;
import lombok.Getter;

@Getter
public class CrewSearchResponse {

    private final Long id;
    private final String name;
    private final Long memberCount;
    private final Long score;

    public CrewSearchResponse(Long id, String name, Long memberCount, Long score) {
        this.id = id;
        this.name = name;
        this.memberCount = memberCount;
        this.score = score;
    }

    public static CrewSearchResponse from(Crew crew) {
        return new CrewSearchResponse(
                crew.getId(),
                crew.getName(),
                crew.getMemberCount(),
                crew.getScore()
        );
    }
}
