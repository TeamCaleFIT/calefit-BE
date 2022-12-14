package com.calefit.workout;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;

import com.calefit.workout.dto.SearchWorkoutResponse;
import com.calefit.workout.entity.Workout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WorkoutServiceTest {

    @InjectMocks
    private WorkoutService workoutService;

    @Mock
    private WorkoutRepository workoutRepository;

    @Test
    void 존재하지_않는_운동종목을_조회하면_비어있는_리스트를_리턴한다() {
        //given
        String notExistWorkoutName = "등록되어 있지 않은 운동";
        List<Workout> emptyWorkouts = new ArrayList<>();

        given(workoutRepository.findByNameContains(notExistWorkoutName)).willReturn(emptyWorkouts);

        //when
        List<SearchWorkoutResponse> searchWorkoutResponses = workoutService.listWorkouts(notExistWorkoutName);

        //then
        assertThat(searchWorkoutResponses)
            .isEmpty();

        then(workoutRepository).should(times(1)).findByNameContains(notExistWorkoutName);
        then(workoutRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void 운동종목명에_조회하기_위한_단어가_포함되어있으면_조회결과로_리턴된다() {
        //given
        String someWordContainedByOverTwoWorkouts = "컬";

        Workout containsSearchingWord1 = new Workout("바벨 컬", "http://s3.aws.com/barbell_curl.png", "바벨 컬은 이런 운동입니다.");
        Workout containsSearchingWord2 = new Workout("케이블 컬", "http://s3.aws.com/cable_curl.png", "케이블 컬은 이런 운동입니다.");
        Workout notContainsSearchingWord = new Workout("스쿼트", "http://s3.aws.com/squat.png", "스쿼트는 이런 운동입니다.");

        List<Workout> workoutsWithMoreThanOneResult = new ArrayList<>();
        workoutsWithMoreThanOneResult.add(containsSearchingWord1);
        workoutsWithMoreThanOneResult.add(containsSearchingWord2);

        List<SearchWorkoutResponse> givenWorkoutsResponse = workoutsWithMoreThanOneResult.stream()
            .map(SearchWorkoutResponse::from)
            .collect(Collectors.toList());

        given(workoutRepository.findByNameContains(someWordContainedByOverTwoWorkouts))
            .willReturn(workoutsWithMoreThanOneResult);

        //when
        List<SearchWorkoutResponse> searchWorkoutResponses = workoutService.listWorkouts(
            someWordContainedByOverTwoWorkouts);

        //then
        assertThat(searchWorkoutResponses)
            .usingRecursiveComparison()
            .isEqualTo(givenWorkoutsResponse);

        then(workoutRepository).should(times(1)).findByNameContains(someWordContainedByOverTwoWorkouts);
        then(workoutRepository).shouldHaveNoMoreInteractions();
    }
}
