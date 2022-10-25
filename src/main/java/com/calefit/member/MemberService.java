package com.calefit.member;

import com.calefit.member.dto.MemberSearchResponse;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberSearchResponse searchMemberProfile(Long Id) {
        //TODO: 로그인한 유저와 요청하는 Member Id가 일치하는지 확인 필요
        Member searchedMember = memberRepository.findById(Id).orElseThrow(NotFoundMemberException::new);
        return MemberSearchResponse.from(searchedMember);
    }
}
