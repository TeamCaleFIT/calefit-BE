package com.calefit.workout.entity;

import static javax.persistence.FetchType.*;

import com.calefit.member.entity.Member;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class Schedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @OneToMany(mappedBy = "schedule")
    private List<WorkoutLog> workoutLogs = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    private Member member;

    public Schedule(LocalDate date, Member member) {
        this.date = date;
        this.member = member;
    }
}
