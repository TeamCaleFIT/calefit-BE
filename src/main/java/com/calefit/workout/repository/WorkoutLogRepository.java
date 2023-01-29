package com.calefit.workout.repository;


import com.calefit.workout.entity.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {

}
