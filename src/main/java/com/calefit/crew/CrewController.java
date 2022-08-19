package com.calefit.crew;

import com.calefit.common.dto.CommonDtoList;
import com.calefit.crew.dto.CrewCreateRequest;
import com.calefit.crew.dto.CrewDeleteRequest;
import com.calefit.crew.dto.CrewDetailedResponse;
import com.calefit.crew.dto.CrewUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<Void> createCrew(@RequestBody CrewCreateRequest crewRequest) {
        crewService.createCrew(crewRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCrew(@PathVariable("id") Long crewId, @RequestBody CrewUpdateRequest crewRequest) {
        crewService.updateCrew(crewId, crewRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
