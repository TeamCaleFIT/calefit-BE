package com.calefit.workout.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Workout {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String description;

}
