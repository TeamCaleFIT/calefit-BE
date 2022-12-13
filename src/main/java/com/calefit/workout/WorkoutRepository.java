package com.calefit.workout;

import com.calefit.workout.entity.Workout;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByNameContains(String name);
}
