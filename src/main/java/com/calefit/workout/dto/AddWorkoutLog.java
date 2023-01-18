package com.calefit.workout.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 하나의 운동 종목에 대한 기록
 * */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddWorkoutLog {

    @Positive(message = "양수 값을 입력해주세요.")
    @NotNull(message = "workout id를 입력해주세요.")
    private Integer workoutId;

    @Positive(message = "양수 값을 입력해주세요.")
    @NotNull(message = "workout id를 입력해주세요.")
    private Integer order;

    private List<AddWorkoutSet> addWorkoutSets;
}
