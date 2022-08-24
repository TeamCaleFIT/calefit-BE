package com.calefit.crew;

import com.calefit.crew.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {

    @Query("SELECT c FROM Crew c WHERE c.name =?1")
    Optional<Crew> findCrewByName(String crewName);

    List<Crew> findCrewsByName(String crewName);
}
