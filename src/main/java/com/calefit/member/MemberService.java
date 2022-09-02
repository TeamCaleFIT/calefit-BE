package com.calefit.member;

import com.calefit.member.dto.MemberSearchResponse;
import com.calefit.member.dto.MemberSignUpRequest;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotAvailableMemberEmailException;
import com.calefit.member.exception.NotAvailableMemberNicknameException;
import com.calefit.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberSearchResponse searchMemberProfile(Long memberId) {
        Member searchedMember = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        return MemberSearchResponse.from(searchedMember);
    }

    @Transactional
    public void signUpMember(MemberSignUpRequest memberRequest) {
        validateDuplicateMemberInfo(memberRequest.getEmail(), memberRequest.getNickname());

        Member member = new Member(
                memberRequest.getEmail(),
                memberRequest.getNickname(),
                passwordEncoder.encode(memberRequest.getPassword()));

        memberRepository.save(member);
    }

    private void validateDuplicateMemberInfo(String email, String nickname) {
        if(memberRepository.existsMemberByEmail(email))
            throw new NotAvailableMemberEmailException();
        if(memberRepository.existsMemberByNickname(nickname))
            throw new NotAvailableMemberNicknameException();
    }
}
