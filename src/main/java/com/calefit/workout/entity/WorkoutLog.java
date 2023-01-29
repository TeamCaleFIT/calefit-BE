package com.calefit.workout.entity;

import static javax.persistence.FetchType.*;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    private Schedule schedule;

    @ManyToOne(fetch = LAZY)
    private Workout workout;

    @OneToMany(mappedBy = "workoutLog", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    List<WorkoutSet> workoutSets = new ArrayList<>();

    public WorkoutLog(Schedule schedule, Workout workout) {
        this.schedule = schedule;
        this.workout = workout;
    }

    public void addWorkoutSets(List<WorkoutSet> workoutSets) {
        this.workoutSets.addAll(workoutSets);
    }
}
