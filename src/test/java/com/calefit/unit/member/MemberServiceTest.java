package com.calefit.unit.member;

import com.calefit.auth.ProviderType;
import com.calefit.member.MemberRepository;
import com.calefit.member.MemberService;
import com.calefit.member.dto.MemberSearchResponse;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotFoundMemberException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("MemberService 단위테스트")
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("24258129", "2@c.com", "Vans", "vansShoes.jpg", ProviderType.KAKAO);
        Field idField = ReflectionUtils.findField(Member.class, "id");
        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.setField(Objects.requireNonNull(idField), member, 1L);
    }

    @Nested
    @DisplayName("멤버 프로필 조회시")
    class SearchMemberProfileTest {

        @Test
        void 멤버Id를_전달받으면_해당_멤버를_조회하고_정보를_반환한다() {

            //given
            given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

            //when
            MemberSearchResponse searchedMemberProfile = memberService.searchMemberProfile(member.getId());

            //then
            assertThat(searchedMemberProfile.getId()).isEqualTo(member.getId());
            assertThat(searchedMemberProfile.getEmail()).isEqualTo(member.getEmail());
            assertThat(searchedMemberProfile.getNickname()).isEqualTo(member.getNickname());

            then(memberRepository).should(times(1)).findById(member.getId());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 존재하지_않는_멤버Id를_전달받으면_예외를_발생_시킨다() {

            //given
            Long notExistMemberId = 2L;
            given(memberRepository.findById(notExistMemberId)).willThrow(new NotFoundMemberException());

            //when

            //then
            assertThatThrownBy(() -> memberRepository.findById(notExistMemberId)).isInstanceOf(NotFoundMemberException.class);

            then(memberRepository).should(times(1)).findById(any());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }
    }
}
