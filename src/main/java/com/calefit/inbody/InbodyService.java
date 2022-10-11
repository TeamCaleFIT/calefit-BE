package com.calefit.inbody;

import com.calefit.inbody.domain.BodyComposition;
import com.calefit.inbody.dto.SearchInbodyResponse;
import com.calefit.inbody.entity.Inbody;
import com.calefit.inbody.exception.NotFoundInbodyException;
import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import com.calefit.member.exception.NotFoundMemberException;
import java.time.LocalDateTime;
import java.util.List;
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
            .orElseThrow(NotFoundMemberException::new);

        return inbodyRepository.save(new Inbody(member, measuredDateTime, bodyComposition));
    }

    @Transactional(readOnly = true)
    public List<SearchInbodyResponse> listInbodies(Long memberId) {
        //todo: pk아닌 회원의 유니크한 id로 조회하도록 변경
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundMemberException();
        }
        List<Inbody> foundInbodies = inbodyRepository.findByMemberId(memberId);

        return foundInbodies.stream()
            .map(SearchInbodyResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteInbody(Long id, Long memberId) {
        //todo: 인증은 나중에 인터셉터에서 처리
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundMemberException();
        }
        if (!inbodyRepository.existsById(id)) {
            throw new NotFoundInbodyException();
        }
        inbodyRepository.deleteById(id);
    }

    @Transactional
    public void updateInbody(Long inbodyId, BodyComposition bodyComposition) {
        Inbody findInbody = inbodyRepository.findById(inbodyId)
            .orElseThrow(NotFoundInbodyException::new);

        findInbody.changeInbody(bodyComposition);
    }
}
