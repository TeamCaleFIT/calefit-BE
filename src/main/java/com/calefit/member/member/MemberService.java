package com.calefit.member.member;

import com.calefit.member.member.dto.MemberLoginRequest;
import com.calefit.member.member.dto.MemberSearchResponse;
import com.calefit.member.member.dto.MemberSignUpRequest;
import com.calefit.member.member.entity.Member;
import com.calefit.member.member.exception.NotAvailableMemberEmailException;
import com.calefit.member.member.exception.NotAvailableMemberLoginException;
import com.calefit.member.member.exception.NotAvailableMemberNicknameException;
import com.calefit.member.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void signUpMember(MemberSignUpRequest memberRequest) {
        validateDuplicateMemberInfo(memberRequest.getEmail(), memberRequest.getNickname());
        Member member = new Member(
                memberRequest.getEmail(),
                memberRequest.getNickname(),
                memberRequest.getPassword());

        memberRepository.save(member);
    }

    @Transactional
    public void loginMember(MemberLoginRequest memberRequest) {
        Member member = memberRepository.findMemberByEmail(memberRequest.getEmail()).orElseThrow(NotFoundMemberException::new);
        if(!member.isPasswordMatched(memberRequest.getPassword())) {
            throw new NotAvailableMemberLoginException();
        }
    }

    @Transactional(readOnly = true)
    public MemberSearchResponse searchMemberProfile(Long memberId) {
        //TODO: 로그인한 유저와 요청하는 Member Id가 일치하는지 확인 필요
        Member searchedMember = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        return MemberSearchResponse.from(searchedMember);
    }

    private void validateDuplicateMemberInfo(String email, String nickname) {
        if (memberRepository.existsMemberByEmail(email)) {
            throw new NotAvailableMemberEmailException();
        }
        if (memberRepository.existsMemberByNickname(nickname)) {
            throw new NotAvailableMemberNicknameException();
        }
    }
}
