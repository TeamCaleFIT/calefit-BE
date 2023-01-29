package com.calefit.workout.dto;

import com.calefit.workout.entity.Workout;
import lombok.Getter;

@Getter
public class SearchWorkoutResponse {

    private final Integer id;
    private final String name;
    private final String imageUrl;
    private final String description;

    public SearchWorkoutResponse(Integer id, String name, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static SearchWorkoutResponse from(Workout workout) {
        return new SearchWorkoutResponse(
            workout.getId(), workout.getName(), workout.getImageUrl(), workout.getDescription()
        );
    }
}
