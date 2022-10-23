package com.calefit.member;

import com.calefit.member.dto.MemberSearchResponse;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotAvailableMemberEmailException;
import com.calefit.member.exception.NotAvailableMemberLoginException;
import com.calefit.member.exception.NotAvailableMemberNicknameException;
import com.calefit.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void signUpMember(String email, String nickname, String password) {
        validateDuplicateMemberInfo(email, nickname);
        Member member = new Member(
                email,
                nickname,
                password);

        memberRepository.save(member);
    }

    @Transactional
    public void loginMember(String email, String password) {
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(NotFoundMemberException::new);
        if(!member.isPasswordMatched(password)) {
            throw new NotAvailableMemberLoginException();
        }
    }

    @Transactional(readOnly = true)
    public MemberSearchResponse searchMemberProfile(Long Id) {
        //TODO: 로그인한 유저와 요청하는 Member Id가 일치하는지 확인 필요
        Member searchedMember = memberRepository.findById(Id).orElseThrow(NotFoundMemberException::new);
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
