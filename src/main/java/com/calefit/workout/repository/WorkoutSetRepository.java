package com.calefit.workout.repository;

import com.calefit.workout.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutSetRepository  extends JpaRepository<WorkoutSet, Long> {

}
