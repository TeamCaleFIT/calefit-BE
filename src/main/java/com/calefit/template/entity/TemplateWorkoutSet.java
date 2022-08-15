package com.calefit.template.entity;

import static javax.persistence.FetchType.*;

import com.calefit.workout.entity.Workout;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
public class TemplateWorkoutSet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Template template;

    @OneToOne(fetch = LAZY)
    private Workout workout;

    private Integer sets;
    private Double goalWeight;
    private Integer goalReps;

}
