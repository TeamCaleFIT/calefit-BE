package com.calefit.workout;

import com.calefit.workout.dto.SearchWorkoutResponse;
import com.calefit.workout.entity.Workout;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public List<SearchWorkoutResponse> listWorkouts(String name) {
        List<Workout> workouts = workoutRepository.findByNameContains(name);
        return workouts.stream()
            .map(SearchWorkoutResponse::from)
            .collect(Collectors.toList());
    }
}
