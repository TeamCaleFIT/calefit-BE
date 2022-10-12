package com.calefit.unit.member;

import com.calefit.member.MemberRepository;
import com.calefit.member.MemberService;
import com.calefit.member.dto.MemberSearchResponse;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotAvailableMemberEmailException;
import com.calefit.member.exception.NotAvailableMemberLoginException;
import com.calefit.member.exception.NotAvailableMemberNicknameException;
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
        member = new Member("2@c.com", "Vans", "12345678");
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

    @Nested
    @DisplayName("멤버 회원가입 요청시")
    class signUpMemberTest {

        @Test
        void 요청_정보를_전달받으면_멤버_회원가입을_완료한다() {

            //given
            String email = "vans@c.com";
            String nickname = "반스";
            String password = "12345678";
            given(memberRepository.existsMemberByEmail(email)).willReturn(false);
            given(memberRepository.existsMemberByNickname(nickname)).willReturn(false);
            doReturn(null).when(memberRepository).save(any());

            //when
            memberService.signUpMember(email, nickname, password);

            //then
            then(memberRepository).should(times(1)).save(any());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 요청_정보중에_이메일_정보가_중복되면_예외를_발생시킨다() {

            //given
            String newMemberDuplicateEmail = "2@c.com";
            given(memberRepository.existsMemberByEmail(newMemberDuplicateEmail)).willThrow(new NotAvailableMemberEmailException());

            //when

            //then
            assertThatThrownBy(() -> memberRepository.existsMemberByEmail(newMemberDuplicateEmail)).isInstanceOf(NotAvailableMemberEmailException.class);

            then(memberRepository).should(times(1)).existsMemberByEmail(any());
            then(memberRepository).should(never()).existsMemberByNickname(any());
            then(memberRepository).should(never()).save(any());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 요청_정보중에_닉네임_정보가_중복되면_예외를_발생시킨다() {

            //given
            String newMemberDuplicateNickname = "Vans";
            given(memberRepository.existsMemberByNickname(newMemberDuplicateNickname)).willThrow(new NotAvailableMemberNicknameException());

            //when

            //then
            assertThatThrownBy(() -> memberRepository.existsMemberByNickname(newMemberDuplicateNickname)).isInstanceOf(NotAvailableMemberNicknameException.class);

            then(memberRepository).should(times(1)).existsMemberByNickname(any());
            then(memberRepository).should(never()).existsMemberByEmail(any());
            then(memberRepository).should(never()).save(any());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("멤버 로그인입 요청시")
    class loginMemberTest {

        @Test
        void 전달받은_멤버Id와_Password가_해당_멤버의_Password와_일치하면_로그인을_완료한다() {

            //given
            String email = "2@c.com";
            String matchedPassword = "12345678";
            given(memberRepository.findMemberByEmail(email)).willReturn(Optional.of(member));

            //when
            memberService.loginMember(email, matchedPassword);

            //then
            then(memberRepository).should(times(1)).findMemberByEmail(email);
            then(memberRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 전달받은_멤버Id와_Password가_해당_멤버의_Password와_일치하지_않으면_예외를_발생시킨다() {

            //given
            String email = "2@c.com";
            String unmatchedPassword = "unmatchedPassword123";
            given(memberRepository.findMemberByEmail(email)).willReturn(Optional.of(member));

            //when
            assertThatThrownBy(() -> memberService.loginMember(email, unmatchedPassword)).isInstanceOf(NotAvailableMemberLoginException.class);

            //then
            then(memberRepository).should(times(1)).findMemberByEmail(email);
            then(memberRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        void 존재하지_않는_멤버Id를_전달받으면_예외를_발생_시킨다() {

            //given
            String notExistMemberEmail = "notExistEmail@c.com";
            given(memberRepository.findMemberByEmail(notExistMemberEmail)).willThrow(new NotFoundMemberException());

            //when

            //then
            assertThatThrownBy(() -> memberRepository.findMemberByEmail(notExistMemberEmail)).isInstanceOf(NotFoundMemberException.class);

            then(memberRepository).should(times(1)).findMemberByEmail(any());
            then(memberRepository).shouldHaveNoMoreInteractions();
        }
    }
}
