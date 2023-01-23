package com.calefit.workout;

import com.calefit.auth.ArgumentReolsver.Auth;
import com.calefit.common.base.ResponseCodes;
import com.calefit.common.dto.CommonDtoList;
import com.calefit.common.entity.CommonResponseEntity;
import com.calefit.workout.dto.AddWorkoutLogsRequest;
import com.calefit.workout.dto.SearchWorkoutResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work-outs")
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping
    public CommonResponseEntity<CommonDtoList<SearchWorkoutResponse>> searchWorkouts(@RequestParam String name) {
        List<SearchWorkoutResponse> workouts = workoutService.listWorkouts(name);

        return new CommonResponseEntity<>(ResponseCodes.WORKOUT_SEARCH_SUCCESS, new CommonDtoList<>(workouts), HttpStatus.OK);
    }


    @PostMapping("/logs")
    public CommonResponseEntity<Void> createWorkoutLog(@RequestBody @Valid AddWorkoutLogsRequest addWorkoutLogsRequest, @Auth String email) {
        workoutService.createWorkoutLog(addWorkoutLogsRequest, email);
        return new CommonResponseEntity<>(ResponseCodes.WORKOUT_LOG_CREATE_SUCCESS, null, HttpStatus.CREATED);
    }
}
