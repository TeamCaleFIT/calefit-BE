package com.calefit.workout.dto;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 그날 하루동안 했던 운동들의 기록
 * */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddWorkoutLogsRequest {

    @NotNull(message = "운동 날짜를 입력해주세요.")
    private LocalDate workoutDate;

    private List<AddWorkoutLog> addWorkoutLogs;
}
