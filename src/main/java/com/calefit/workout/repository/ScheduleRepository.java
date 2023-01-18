package com.calefit.workout.repository;

import com.calefit.workout.entity.Schedule;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByDate(LocalDate date);
}
