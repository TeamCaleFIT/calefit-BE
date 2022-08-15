package com.calefit.workout.entity;

import static javax.persistence.FetchType.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class WorkoutLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Schedule schedule;

    @OneToOne(fetch = LAZY)
    private Workout workout;

    //todo: 아래 정보로 따로 처리할 작업이 있다면 추후 Embeddable로 분리하기.
    private Integer sets; //set은 db예약어라 sets로 변경. h2의 예약어인지 mysql의 예약어인지는 확인 필요.
    private Double goalWeight;
    private Integer goalReps;
    private Double finishedWeight;
    private Integer finishedReps;

}
