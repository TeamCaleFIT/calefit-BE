package com.calefit.inbody;

import com.calefit.inbody.domain.BodyComposition;
import com.calefit.inbody.entity.Inbody;
import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
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
}

