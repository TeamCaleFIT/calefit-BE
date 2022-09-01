package com.calefit.inbody;

import com.calefit.inbody.domain.BodyComposition;
import com.calefit.inbody.dto.SearchInbodyResponse;
import com.calefit.inbody.entity.Inbody;
import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InbodyService {

    private final InbodyRepository inbodyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Inbody createInbody(Long memberId, LocalDateTime measuredDateTime, BodyComposition bodyComposition) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(NoSuchElementException::new);

        return inbodyRepository.save(new Inbody(member, measuredDateTime, bodyComposition));
    }

    public List<SearchInbodyResponse> listInbodies(Long memberId) {
        //todo: pk아닌 회원의 유니크한 id로 조회하도록 변경
        if (!memberRepository.existsById(memberId)) {
            throw new NoSuchElementException("존재하지 않는 회원 id입니다.");
        }
        List<Inbody> foundInbodies = inbodyRepository.findByMemberId(memberId);

        return foundInbodies.stream()
            .map(SearchInbodyResponse::from)
            .collect(Collectors.toList());
    }

}
