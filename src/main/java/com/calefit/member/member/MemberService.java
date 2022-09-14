package com.calefit.member.member;

import com.calefit.member.member.dto.MemberSearchResponse;
import com.calefit.member.member.dto.MemberSignUpRequest;
import com.calefit.member.member.entity.Member;
import com.calefit.member.member.exception.NotAvailableMemberEmailException;
import com.calefit.member.member.exception.NotAvailableMemberNicknameException;
import com.calefit.member.member.exception.NotFoundMemberException;
import com.calefit.member.member.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUpMember(MemberSignUpRequest memberRequest) {
        validateDuplicateMemberInfo(memberRequest.getEmail(), memberRequest.getNickname());

        String[] saltAndPassword = passwordEncoder.hashing(memberRequest.getPassword(), null);
        String salt = saltAndPassword[0];
        String password = saltAndPassword[1];

        Member member = new Member(
                memberRequest.getEmail(),
                memberRequest.getNickname(),
                salt,
                password);

        memberRepository.save(member);
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
