package com.calefit.crew;

import com.calefit.common.dto.CommonDtoList;
import com.calefit.crew.dto.CrewCreateRequest;
import com.calefit.crew.dto.CrewDetailedResponse;
import com.calefit.crew.dto.CrewSearchResponse;
import com.calefit.crew.entity.Crew;
import com.calefit.member.MemberRepository;
import com.calefit.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public CommonDtoList searchCrews() {
        List<Crew> searchCrews = crewRepository.findAll();
        //TODO searchCrews에 대한 exception 처리 필요
        List<CrewSearchResponse> crewSearchResponses = searchCrews
                .stream()
                .map(CrewSearchResponse::from)
                .collect(Collectors.toList());

        return new CommonDtoList(crewSearchResponses);
    }

    @Transactional(readOnly = true)
    public CrewDetailedResponse searchCrewById (Long crewId) {
        Optional<Crew> searchCrewById = crewRepository.findById(crewId);
        //TODO searchCrewById에 대한 exception 처리 필요

        return CrewDetailedResponse.from(searchCrewById.orElseThrow(RuntimeException::new));
    }

    @Transactional(readOnly = true)
    public CommonDtoList searchCrewByName(String crewName) {
        List<Crew> searchCrews = crewRepository.findCrewsByName(crewName);
        //TODO searchCrews에 대한 exception 처리 필요
        List<CrewSearchResponse> crewSearchResponses = searchCrews
                .stream()
                .map(CrewSearchResponse::from)
                .collect(Collectors.toList());

        return new CommonDtoList(crewSearchResponses);
    }

    @Transactional
    public void createCrew(CrewCreateRequest crewRequest) {
        Long memberId = crewRequest.getMemberId();
        Member captain = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        //TODO 이미 크루장인지 검증 필요
        captain.editCrewInfo(true, "captain");
        //TODO 크루이름 중복 검증 필요
        crewRepository.save(new Crew(crewRequest.getName(),
                                     crewRequest.getDescription(),
                                     crewRequest.getImage()));
    }
}
