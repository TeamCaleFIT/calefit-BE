package com.calefit.member;

import com.calefit.member.dto.MemberSearchResponse;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberSearchResponse searchMemberProfile(Long memberId) {
        Member searchedMember = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        return MemberSearchResponse.from(searchedMember);
    }
}
