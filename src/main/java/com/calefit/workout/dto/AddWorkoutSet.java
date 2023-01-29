package com.calefit.workout.dto;

import com.calefit.workout.entity.WorkoutLog;
import com.calefit.workout.entity.WorkoutSet;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 운동 한 종목에 대한 세트 하나의 기록
 * */
@Getter
@NoArgsConstructor
public class AddWorkoutSet {

    @NotNull(message = "세트 번호를 입력해주세요.")
    @Min(value = 1, message = "세트 번호는 1 이상의 값을 입력해주세요.")
    private Integer setNumber;

    @NotNull(message = "목표 무게를 입력해주세요.")
    @Positive(message = "양수 값을 입력해주세요.")
    private Double goalWeight;

    @NotNull(message = "목표 횟수를 입력해주세요.")
    @Min(value = 1, message = "목표 횟수는 1 이상의 값을 입력해주세요.")
    private Integer goalReps;

    @NotNull(message = "수행한 무게를 입력해주세요.")
    @Positive(message = "양수 값을 입력해주세요.")
    private Double finishedWeight;

    @NotNull(message = "수행 횟수를 입력해주세요.")
    @Positive(message = "수행 횟수는 1 이상의 값을 입력해주세요.")
    private Integer finishedReps;

    public WorkoutSet toEntity(WorkoutLog workoutLog) {
        return new WorkoutSet(workoutLog, setNumber, goalWeight, goalReps, finishedWeight, finishedReps);
    }
}
