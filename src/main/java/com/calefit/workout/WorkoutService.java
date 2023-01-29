package com.calefit.workout;

import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotFoundMemberException;
import com.calefit.workout.dto.AddWorkoutLog;
import com.calefit.workout.dto.AddWorkoutLogsRequest;
import com.calefit.workout.dto.SearchWorkoutResponse;
import com.calefit.workout.entity.Schedule;
import com.calefit.workout.entity.Workout;
import com.calefit.workout.entity.WorkoutLog;
import com.calefit.workout.entity.WorkoutSet;
import com.calefit.workout.exception.NotFoundWorkoutException;
import com.calefit.workout.repository.WorkoutLogRepository;
import com.calefit.workout.repository.WorkoutRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutLogRepository workoutLogRepository;
    private final MemberRepository memberRepository;

    public List<SearchWorkoutResponse> listWorkouts(String name) {
        List<Workout> workouts = workoutRepository.findByNameContains(name);
        return workouts.stream()
            .map(SearchWorkoutResponse::from)
            .collect(Collectors.toList());
    }
    /**
     * 그날 Schedule에 해당하는 WorkoutLog 최초 등록시
     * */
    @Transactional
    public void createWorkoutLog(AddWorkoutLogsRequest addWorkoutLogsRequest, String email) {
        Member member = memberRepository.findMemberByEmail(email)
            .orElseThrow(NotFoundMemberException::new);

        Schedule schedule = new Schedule(LocalDate.now(), member);

        for (AddWorkoutLog addWorkoutLog : addWorkoutLogsRequest.getAddWorkoutLogs()) {
            Workout workout = findByIdOrElseThrow(addWorkoutLog.getWorkoutId());
            WorkoutLog workoutLog = new WorkoutLog(schedule, workout);

            List<WorkoutSet> workoutSets = addWorkoutLog.getAddWorkoutSets().stream()
                .map(addWorkoutSet -> addWorkoutSet.toEntity(workoutLog))
                .collect(Collectors.toList());

            workoutLog.addWorkoutSets(workoutSets);

            workoutLogRepository.save(workoutLog);
        }
    }

    private Workout findByIdOrElseThrow(Integer workoutId) {
        return workoutRepository.findById(workoutId)
            .orElseThrow(NotFoundWorkoutException::new);
    }
}
