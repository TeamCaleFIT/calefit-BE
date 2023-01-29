package com.calefit.workout.entity;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkoutSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    private WorkoutLog workoutLog;

    private int setNumber;
    private double goalWeight;
    private int goalReps;
    private double finishedWeight;
    private int finishedReps;

    public WorkoutSet(WorkoutLog workoutLog, int setNumber, double goalWeight,
        int goalReps, double finishedWeight, int finishedReps) {
        this.workoutLog = workoutLog;
        this.setNumber = setNumber;
        this.goalWeight = goalWeight;
        this.goalReps = goalReps;
        this.finishedWeight = finishedWeight;
        this.finishedReps = finishedReps;
    }
}
