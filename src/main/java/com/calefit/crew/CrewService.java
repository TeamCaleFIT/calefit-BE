package com.calefit.crew;

import com.calefit.common.dto.CommonDtoList;
import com.calefit.crew.dto.*;
import com.calefit.crew.entity.Crew;
import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public CommonDtoList searchCrews() {
        List<Crew> searchCrews = crewRepository.findAll();
        validCrewsIsEmpty(searchCrews);

        List<CrewSearchResponse> crewSearchResponses = searchCrews
                .stream()
                .map(CrewSearchResponse::from)
                .collect(Collectors.toList());

        return new CommonDtoList(crewSearchResponses);
    }

    @Transactional(readOnly = true)
    public CrewDetailedResponse searchCrewById(Long crewId) {

        return CrewDetailedResponse.from(validIsCrewExist(crewId));
    }

    @Transactional(readOnly = true)
    public CommonDtoList searchCrewByName(String crewName) {
        List<Crew> searchCrews = crewRepository.findCrewsByName(crewName);
        validCrewsIsEmpty(searchCrews);

        List<CrewSearchResponse> crewSearchResponses = searchCrews
                .stream()
                .map(CrewSearchResponse::from)
                .collect(Collectors.toList());

        return new CommonDtoList(crewSearchResponses);
    }

    @Transactional
    public void createCrew(CrewCreateRequest crewRequest) {
        Member member = validMemberIsExist(crewRequest.getMemberId());
        validDuplicateCrew(member);
        validDuplicateName(crewRequest.getName());

        Crew saved = crewRepository.save(new Crew(crewRequest.getName(),
                crewRequest.getDescription(),
                crewRequest.getImage()));

        member.addCrew(saved);
    }

    @Transactional
    public void updateCrew(Long crewId, CrewUpdateRequest crewRequest) {
        Member member = validMemberIsExist(crewRequest.getMemberId());
        validCaptainAuthority(crewId, member, member.getCrew().getId());
        validDuplicateName(crewRequest.getName());
        Crew crew = validIsCrewExist(crewId);

        crew.updateCrew(crewRequest.getName(),
                crewRequest.getDescription(),
                crewRequest.getImage());
    }

    @Transactional
    public void deleteCrew(Long crewId, CrewDeleteRequest crewRequest) {
        Member member = validMemberIsExist(crewRequest.getMemberId());
        validCaptainAuthority(crewId, member, member.getCrew().getId());
        Crew crew = validIsCrewExist(crewId);

        member.removeCaptainAuthority();
        memberRepository.save(member);
        memberRepository.updateCrewIdOfMembers(crewId);

        crew.delete();
        crewRepository.save(crew);
    }

    private void validCrewsIsEmpty(List<Crew> searchCrews) {
        if (CollectionUtils.isEmpty(searchCrews)) {
            throw new IllegalStateException("크루가 존재하지 않습니다.");
        }
    }

    private Crew validIsCrewExist(Long crewId) {
        return crewRepository.findById(crewId).orElseThrow(() -> new IllegalStateException("크루가 존재하지 않습니다."));
    }

    private Member validMemberIsExist(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalStateException("유저가 존재하지 않습니다."));
    }

    private void validCaptainAuthority(Long crewId, Member member, Long findCrewId) {
        if (!Objects.equals(crewId, findCrewId) || !Objects.equals(member.getCrewInfo().getRole(), "captain")) {
            throw new IllegalStateException("권한이 없습니다.");
        }
    }

    private void validDuplicateCrew(Member member) {
        if (Objects.equals(member.getCrewInfo().getRole(), "captain")) {
            throw new IllegalStateException("이미 크루를 소유하고 있습니다.");
        }
    }

    private void validDuplicateName(String crewName) {
        if (!Objects.isNull(crewRepository.findCrewByName(crewName))) {
            throw new IllegalStateException("중복된 이름입니다.");
        }
    }
}
