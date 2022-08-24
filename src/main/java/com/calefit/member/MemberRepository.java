package com.calefit.member;

import com.calefit.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.crew.id = null where m.crew.id = ?1")
    void updateCrewIdOfMembers(Long crewId);
}
