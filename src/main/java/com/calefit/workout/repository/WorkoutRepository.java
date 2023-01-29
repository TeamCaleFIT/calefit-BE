package com.calefit.workout.repository;

import com.calefit.workout.entity.Workout;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Integer> {

    List<Workout> findByNameContains(String name);
}
