package com.calefit.unit.member;

import com.calefit.member.MemberRepository;
import com.calefit.member.MemberService;
import com.calefit.member.dto.MemberSearchResponse;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotFoundMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService 단위테스트")
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;

    private Member member;
    private Long EXCEPTION_NUMBER;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "2@c.com", "Vans", "1234");
    }

    @Nested
    @DisplayName("멤버 프로필 조회 클래스")
    class SearchMemberProfileTest {

        @Test
        @DisplayName("멤버 프로필 조회 기능 테스트")
        void 멤버Id를_전달받으면_해당_멤버를_조회하고_정보를_반환한다() {

            //given
            given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

            //when
            MemberSearchResponse searchedMemberProfile = memberService.searchMemberProfile(member.getId());

            //then
            assertThat(searchedMemberProfile.getId()).isEqualTo(member.getId());
            assertThat(searchedMemberProfile.getEmail()).isEqualTo(member.getEmail());
            assertThat(searchedMemberProfile.getNickname()).isEqualTo(member.getNickname());
            assertThat(searchedMemberProfile.getBodyInfo()).isNull();
        }

        @Test
        @DisplayName("멤버 프로필 조회 예외 테스트")
        void 존재하지_않는_멤버Id를_전달받으면_예외를_발생_시킨다() {

            //given
            EXCEPTION_NUMBER = 2L;
            given(memberRepository.findById(EXCEPTION_NUMBER)).willThrow(new NotFoundMemberException());

            //when

            //then
            assertThatThrownBy(() -> memberRepository.findById(EXCEPTION_NUMBER)).isInstanceOf(NotFoundMemberException.class);
        }
    }
}