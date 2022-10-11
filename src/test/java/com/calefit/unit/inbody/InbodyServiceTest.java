package com.calefit.unit.inbody;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.calefit.inbody.InbodyRepository;
import com.calefit.inbody.InbodyService;
import com.calefit.inbody.domain.BodyComposition;
import com.calefit.inbody.dto.SearchInbodyResponse;
import com.calefit.inbody.entity.Inbody;
import com.calefit.inbody.exception.NotFoundInbodyException;
import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotFoundMemberException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InbodyServiceTest {

    @InjectMocks
    private InbodyService inbodyService;

    @Mock
    private InbodyRepository inbodyRepository;

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void memberSetup() {
        member = new Member("pio@gamil.com", "pio", "1234");
        ReflectionTestUtils.setField(member, "id", 1L);
    }

    private Inbody createInbodyDummy(Member member, LocalDateTime measuredTime, BodyComposition bodyComposition, Long inbodyId) {
        Inbody inbody = new Inbody(member, measuredTime, bodyComposition);
        ReflectionTestUtils.setField(inbody, "id", inbodyId);
        return inbody;
    }

    @Nested
    @DisplayName("인바디 등록시")
    class InbodyCreate {

        @Test
        void 존재하는_멤버_id와_인바디_측정시간_인바디_정보를_전달받으면_멤버를_조회한_후_인바디를_저장한다() {
            //given
            LocalDateTime measuredTime = LocalDateTime.of(2022, 8, 20, 13, 5);
            BodyComposition bodyComposition = new BodyComposition(30.0, 17.5, 65.0);
            Inbody newInbody = createInbodyDummy(member, measuredTime, bodyComposition, 1L);

            given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
            given(inbodyRepository.save(any())).willReturn(newInbody);

            //when
            Inbody savedInbody = inbodyService.createInbody(member.getId(), measuredTime, bodyComposition);

            //then
            assertThat(savedInbody.getId()).isEqualTo(newInbody.getId());

            then(memberRepository).should(times(1)).findById(member.getId());
            then(inbodyRepository).should(times(1)).save(any());
            then(inbodyRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 존재하지_않는_회원id를_전달받으면_NotFoundMemberException_예외가_발생하고_인바디_등록에_실패한다() {
            //given
            Long notExistMemberId = 2L;
            LocalDateTime measuredTime = LocalDateTime.of(2022, 8, 20, 13, 5);
            BodyComposition bodyComposition = new BodyComposition(30.0, 17.5, 65.0);
            Inbody newInbody = new Inbody(member, measuredTime, bodyComposition);

            given(memberRepository.findById(notExistMemberId)).willThrow(new NotFoundMemberException());

            //when

            //then
            assertThatThrownBy(() -> inbodyService.createInbody(notExistMemberId, measuredTime, bodyComposition))
                .isInstanceOf(NotFoundMemberException.class);

            then(memberRepository).should(times(1)).findById(notExistMemberId);
            then(inbodyRepository).should(never()).save(newInbody);
            then(inbodyRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("인바디 전체 조회시")
    class InbodySearch {

        @Test
        void 존재하는_멤버_id를_전달받으면_멤버_확인_후_해당_멤버의_전체_인바디_정보를_조회하여_각각_dto로_변환하여_리스트에_담아_리턴한다() {
            //given
            LocalDateTime measuredTime1 = LocalDateTime.of(2022, 1, 20, 13, 5);
            LocalDateTime measuredTime2 = LocalDateTime.of(2022, 2, 20, 13, 5);
            BodyComposition bodyComposition1 = new BodyComposition(31.0, 17.5, 65.0);
            BodyComposition bodyComposition2 = new BodyComposition(32.0, 17.5, 65.0);

            Inbody inbody1 = new Inbody(member, measuredTime1, bodyComposition1);
            Inbody inbody2 = new Inbody(member, measuredTime2, bodyComposition2);

            List<Inbody> findInbodies = new ArrayList<>();
            findInbodies.add(inbody1);
            findInbodies.add(inbody2);

            List<SearchInbodyResponse> givenInbodyResponses = findInbodies.stream()
                .map(SearchInbodyResponse::from)
                .collect(Collectors.toList());

            given(memberRepository.existsById(member.getId())).willReturn(true);
            given(inbodyRepository.findByMemberId(member.getId())).willReturn(findInbodies);

            //when
            List<SearchInbodyResponse> inbodyResponses = inbodyService.listInbodies(member.getId());

            //then
            assertThat(inbodyResponses)
                .usingRecursiveComparison()
                .isEqualTo(givenInbodyResponses);

            then(memberRepository).should(times(1)).existsById(member.getId());
            then(inbodyRepository).should(times(1)).findByMemberId(member.getId());
            then(inbodyRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 존재하지_않는_회원id를_전달받으면_NotFoundMemberException_예외가_발생하고_인바디_등록에_실패한다() {
            //given
            Long notExistMemberId = 2L;
            LocalDateTime measuredTime = LocalDateTime.of(2022, 8, 20, 13, 5);
            BodyComposition bodyComposition = new BodyComposition(30.0, 17.5, 65.0);
            Inbody newInbody = new Inbody(member, measuredTime, bodyComposition);

            given(memberRepository.findById(notExistMemberId)).willThrow(new NotFoundMemberException());

            //when

            //then
            assertThatThrownBy(() -> inbodyService.createInbody(notExistMemberId, measuredTime, bodyComposition))
                .isInstanceOf(NotFoundMemberException.class);

            then(memberRepository).should(times(1)).findById(notExistMemberId);
            then(inbodyRepository).should(never()).save(newInbody);
            then(inbodyRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("인바디 삭제시")
    class 인바디_삭제시 {

        @Test
        void 존재하는_멤버_id를_전달받으면_멤버_확인_후_인바디를_삭제한다() {
            //given
            Long inbodyIdForDelete = 1L;

            given(memberRepository.existsById(member.getId())).willReturn(true);
            given(inbodyRepository.existsById(inbodyIdForDelete)).willReturn(true);
            doNothing().when(inbodyRepository).deleteById(inbodyIdForDelete);

            //when
            inbodyService.deleteInbody(inbodyIdForDelete, member.getId());

            //then
            assertDoesNotThrow(() -> new NotFoundMemberException());

            then(memberRepository).should(times(1)).existsById(member.getId());
            then(inbodyRepository).should(times(1)).existsById(inbodyIdForDelete);
            then(inbodyRepository).should(times(1)).deleteById(inbodyIdForDelete);
            then(inbodyRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 존재하지_않는_회원_id를_전달받으면_NotFoundMemberException_예외가_발생하고_인바디_삭제에_실패한다() {
            //given
            Long notExistMemberId = 2L;
            Long inbodyIdForDelete = 1L;

            given(memberRepository.existsById(notExistMemberId)).willThrow(new NotFoundMemberException());

            //when

            //then
            assertThatThrownBy(() -> inbodyService.deleteInbody(inbodyIdForDelete, notExistMemberId))
                .isInstanceOf(NotFoundMemberException.class);

            then(memberRepository).should(times(1)).existsById(notExistMemberId);
            then(inbodyRepository).should(never()).existsById(inbodyIdForDelete);
            then(inbodyRepository).should(never()).deleteById(inbodyIdForDelete);
            then(inbodyRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 존재하지_않는_인바디_id를_전달받으면_NotFoundInbodyException_예외가_발생하고_인바디_삭제에_실패한다() {
            //given
            Long notExistInbodyId = 1L;

            given(memberRepository.existsById(member.getId())).willReturn(true);
            given(inbodyRepository.existsById(notExistInbodyId)).willThrow(new NotFoundInbodyException());

            //when

            //then
            assertThatThrownBy(() -> inbodyService.deleteInbody(notExistInbodyId, member.getId()))
                .isInstanceOf(NotFoundInbodyException.class);

            then(memberRepository).should(times(1)).existsById(member.getId());
            then(inbodyRepository).should(times(1)).existsById(notExistInbodyId);
            then(inbodyRepository).should(never()).deleteById(notExistInbodyId);
            then(inbodyRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("인바디 수정시")
    class InbodyUpdate {

        @Test
        void 존재하는_인바디_id와_기존_측정시간_및_변경할_BodyComposition을_전달받으면_해당_정보로_인바디를_수정한다() {
            //given
            LocalDateTime measuredDateTime = LocalDateTime.of(1111, 1, 1, 1, 1);
            BodyComposition bodyComposition = new BodyComposition(10.0, 10.0, 10.0);
            Inbody inbody = createInbodyDummy(member, measuredDateTime, bodyComposition, 1L);

            Long existInbodyId = 1L;
            BodyComposition updateBodyComposition = new BodyComposition(50.0, 50.0, 50.0);
            Inbody updateInbody = createInbodyDummy(member, measuredDateTime, updateBodyComposition, existInbodyId);

            given(inbodyRepository.findById(existInbodyId)).willReturn(Optional.of(inbody));

            //when
            inbodyService.updateInbody(existInbodyId, updateBodyComposition);

            //then
            assertThat(existInbodyId).isEqualTo(updateInbody.getId());

            assertThat(inbody.getMeasuredDateTime()).isEqualTo(updateInbody.getMeasuredDateTime()); //측정일자 및 시간은 변경 불가능.

            assertThat(inbody.getBodyComposition().getMuscle()).isEqualTo(updateBodyComposition.getMuscle());
            assertThat(inbody.getBodyComposition().getBodyWeight()).isEqualTo(updateBodyComposition.getBodyWeight());
            assertThat(inbody.getBodyComposition().getBodyWeight()).isEqualTo(updateBodyComposition.getBodyWeight());

            then(inbodyRepository).should(times(1)).findById(existInbodyId);
            then(inbodyRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 존재하지_않는_인바디_id를_전달받으면_NotFoundInbodyException_예외가_발생하고_인바디_수정에_실패한다() {
            //given
            Long notExistInbodyId = 9999L;
            BodyComposition updateBodyComposition = new BodyComposition(50.0, 50.0, 50.0);

            given(inbodyRepository.findById(notExistInbodyId)).willThrow(new NotFoundInbodyException());

            //when

            //then
            assertThatThrownBy(() -> inbodyService.updateInbody(notExistInbodyId, updateBodyComposition))
                .isInstanceOf(NotFoundInbodyException.class);

            then(inbodyRepository).should(times(1)).findById(notExistInbodyId);
            then(inbodyRepository).shouldHaveNoMoreInteractions();
        }
    }
}
