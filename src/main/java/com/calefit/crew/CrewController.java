package com.calefit.crew;

import com.calefit.common.dto.CommonDtoList;
import com.calefit.crew.dto.CrewSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crews")
public class CrewController {

    private final CrewService crewService;

    @GetMapping
    public ResponseEntity<CommonDtoList> searchCrews() {
        return ResponseEntity.ok(crewService.searchCrews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrewSearchResponse> searchCrewById(@PathVariable Long id) {
        return ResponseEntity.ok(crewService.searchCrewById(id));
    }
}
