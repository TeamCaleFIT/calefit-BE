package com.calefit.crew;

import com.calefit.crew.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CrewRepository extends JpaRepository<Crew, Long> {

    @Query("SELECT c FROM Crew c WHERE c.name =?1")
    Crew findCrewByName(String crewName);

    @Query("SELECT name FROM Crew WHERE name LIKE '%:crewName%'")
    List<Crew> findCrewsByName(@Param("name") String crewName);
}
