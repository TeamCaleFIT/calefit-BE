package com.calefit.template.entity;

import static javax.persistence.FetchType.*;

import com.calefit.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Template {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "template")
    private List<TemplateWorkoutSet> templateWorkoutSets = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    private Member member;

}
