package com.calefit.crew;

import com.calefit.common.dto.CommonDtoList;
import com.calefit.crew.dto.CrewSearchResponse;
import com.calefit.crew.entity.Crew;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;

    @Transactional(readOnly = true)
    public CommonDtoList searchCrews() {
        List<Crew> searchCrews = crewRepository.findAll();

        List<CrewSearchResponse> crewSearchResponses = searchCrews
                .stream()
                .map(CrewSearchResponse::from)
                .collect(Collectors.toList());

        return new CommonDtoList(crewSearchResponses);
    }
}
