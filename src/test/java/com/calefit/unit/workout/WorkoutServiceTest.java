package com.calefit.unit.workout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.calefit.auth.domain.provider.ProviderType;
import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotFoundMemberException;
import com.calefit.workout.WorkoutService;
import com.calefit.workout.dto.AddWorkoutLog;
import com.calefit.workout.dto.AddWorkoutLogsRequest;
import com.calefit.workout.dto.AddWorkoutSet;
import com.calefit.workout.dto.SearchWorkoutResponse;
import com.calefit.workout.entity.Schedule;
import com.calefit.workout.entity.Workout;
import com.calefit.workout.entity.WorkoutLog;
import com.calefit.workout.repository.WorkoutLogRepository;
import com.calefit.workout.repository.WorkoutRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WorkoutServiceTest {

    @InjectMocks
    private WorkoutService workoutService;

    @Mock
    private WorkoutRepository workoutRepository;
    @Mock
    private WorkoutLogRepository workoutLogRepository;
    @Mock
    private MemberRepository memberRepository;

    private Member member;


    @BeforeEach
    void setUp() {
        member = new Member("123456789", "pio@naver.com", "pio", "pioFace.jpg", ProviderType.KAKAO);
        ReflectionTestUtils.setField(member, "id", 1L);
    }

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

    @Nested
    class 운동_로그_등록_시 {

        @Test
        void 존재하는_멤버_email과_운동_기록을_전달받으면_당일_Schedule을_생성하고_운동_로그를_등록한다()
            throws JsonProcessingException {
            //given
            AddWorkoutLogsRequest addWorkoutLogsRequest = createDummyRequestWorkoutLogsDto();

            LocalDate workoutDate = LocalDate.of(2022, 12, 27);
            Schedule schedule = new Schedule(workoutDate, member);

            for (AddWorkoutLog addWorkoutLog : addWorkoutLogsRequest.getAddWorkoutLogs()) {
                //Workout
                Workout workout = createDummyWorkout(addWorkoutLog.getWorkoutId());
                given(workoutRepository.findById(addWorkoutLog.getWorkoutId())).willReturn(Optional.of(workout));
                given(memberRepository.findMemberByEmail(member.getEmail())).willReturn(Optional.of(member));

                //WorkoutLog
                WorkoutLog workoutLog = new WorkoutLog(schedule, workout);
                given(workoutLogRepository.save(any())).willReturn(workoutLog);
            }

            //when
            workoutService.createWorkoutLog(addWorkoutLogsRequest, member.getEmail());

            //then
            assertDoesNotThrow(() -> new NotFoundMemberException());

            then(memberRepository).should(times(1)).findMemberByEmail(member.getEmail());
            then(workoutRepository).should(times(addWorkoutLogsRequest.getAddWorkoutLogs().size())).findById(any());
            then(workoutLogRepository).should(times(addWorkoutLogsRequest.getAddWorkoutLogs().size())).save(any());
            then(workoutLogRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 존재하지_않는_멤버_email을_전달받으면_NotFoundMemberException_예외가_발생하고_운동_로그_등록에_실패한다()
            throws JsonProcessingException {
            //given
            String notExistMemberEmail = "NOT_EXIST_EMAIL@naver.com";
            AddWorkoutLogsRequest addWorkoutLogsRequest = createDummyRequestWorkoutLogsDto();

            given(memberRepository.findMemberByEmail(notExistMemberEmail)).willThrow(new NotFoundMemberException());

            //when

            //then
            assertThatThrownBy(() -> workoutService.createWorkoutLog(addWorkoutLogsRequest, notExistMemberEmail))
                .isInstanceOf(NotFoundMemberException.class);
            //then
            then(memberRepository).should(times(1)).findMemberByEmail(notExistMemberEmail);
            then(workoutLogRepository).should(never()).save(any(WorkoutLog.class));
            then(workoutLogRepository).shouldHaveNoMoreInteractions();
        }
    }

    private AddWorkoutLogsRequest createDummyRequestWorkoutLogsDto()
        throws JsonProcessingException {
        //create AddWorkoutSet1
        AddWorkoutSet addWorkoutSet1 = new AddWorkoutSet();
        ReflectionTestUtils.setField(addWorkoutSet1, "setNumber", 1);
        ReflectionTestUtils.setField(addWorkoutSet1, "goalWeight", 50.0);
        ReflectionTestUtils.setField(addWorkoutSet1, "goalReps", 12);
        ReflectionTestUtils.setField(addWorkoutSet1, "finishedWeight", 50.0);
        ReflectionTestUtils.setField(addWorkoutSet1, "finishedReps", 12);
        List<AddWorkoutSet> addWorkoutSets1 = new ArrayList<>();
        addWorkoutSets1.add(addWorkoutSet1);

        //create AddWorkoutLog1
        AddWorkoutLog addWorkoutLog1 = new AddWorkoutLog();
        ReflectionTestUtils.setField(addWorkoutLog1, "workoutId", 1);
        ReflectionTestUtils.setField(addWorkoutLog1, "order", 1);
        ReflectionTestUtils.setField(addWorkoutLog1, "addWorkoutSets", addWorkoutSets1);

        //create AddWorkoutSet2
        AddWorkoutSet addWorkoutSet2 = new AddWorkoutSet();
        ReflectionTestUtils.setField(addWorkoutSet2, "setNumber", 1);
        ReflectionTestUtils.setField(addWorkoutSet2, "goalWeight", 50.0);
        ReflectionTestUtils.setField(addWorkoutSet2, "goalReps", 12);
        ReflectionTestUtils.setField(addWorkoutSet2, "finishedWeight", 50.0);
        ReflectionTestUtils.setField(addWorkoutSet2, "finishedReps", 12);
        List<AddWorkoutSet> addWorkoutSets2 = new ArrayList<>();
        addWorkoutSets2.add(addWorkoutSet2);

        //create AddWorkoutLog2
        AddWorkoutLog addWorkoutLog2 = new AddWorkoutLog();
        ReflectionTestUtils.setField(addWorkoutLog2, "workoutId", 2);
        ReflectionTestUtils.setField(addWorkoutLog2, "order", 1);
        ReflectionTestUtils.setField(addWorkoutLog2, "addWorkoutSets", addWorkoutSets2);

        //create AddWorkoutLogsRequeest
        List<AddWorkoutLog> addWorkoutLogs = new ArrayList<>();
        addWorkoutLogs.add(addWorkoutLog1);
        addWorkoutLogs.add(addWorkoutLog2);

        AddWorkoutLogsRequest addWorkoutLogsRequest = new AddWorkoutLogsRequest();
        ReflectionTestUtils.setField(addWorkoutLogsRequest, "workoutDate",
            LocalDate.of(2022, 12, 27));
        ReflectionTestUtils.setField(addWorkoutLogsRequest, "addWorkoutLogs", addWorkoutLogs);

        return addWorkoutLogsRequest;
    }

    private Workout createDummyWorkout(Integer workoutId) {
        Workout workout = new Workout("운동명", "이미지 url", "운동 설명");
        ReflectionTestUtils.setField(workout, "id", workoutId);
        return workout;
    }
}
