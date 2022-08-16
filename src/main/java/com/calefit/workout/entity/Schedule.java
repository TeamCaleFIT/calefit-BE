package com.calefit.workout.entity;

import static javax.persistence.FetchType.*;

import com.calefit.member.entity.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Schedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @OneToMany(mappedBy = "schedule")
    private List<WorkoutLog> workoutLogs = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    private Member member;
}
