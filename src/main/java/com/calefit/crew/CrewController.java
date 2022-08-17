package com.calefit.crew;

import com.calefit.common.dto.CommonDtoList;
import com.calefit.crew.dto.CrewDetailedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CrewDetailedResponse> searchCrewById(@PathVariable("id") Long crewId) {
        return ResponseEntity.ok(crewService.searchCrewById(crewId));
    }


    @GetMapping("/name")
    public ResponseEntity<CommonDtoList> searchCrewByName(@RequestParam("name") String crewName) {
        return ResponseEntity.ok(crewService.searchCrewByName(crewName));
    }
}
