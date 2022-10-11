package com.calefit.inbody;

import com.calefit.inbody.entity.Inbody;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InbodyRepository extends JpaRepository<Inbody, Long> {



    @Query("select i from Inbody i inner join i.member m where m.id = :memberId")
    List<Inbody> findByMemberId(@Param("memberId") Long memberId);
}

