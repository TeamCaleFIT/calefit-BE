package com.calefit.inbody;

import com.calefit.inbody.entity.Inbody;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InbodyRepository extends JpaRepository<Inbody, Long> {

}

