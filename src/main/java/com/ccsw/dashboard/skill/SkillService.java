package com.ccsw.dashboard.skill;

import java.util.List;

import com.ccsw.dashboard.skill.model.Skill;

public interface SkillService {

    // Skill findById(Long id) throws EntityNotFoundException;
    List<Skill> findAll();

    List<Skill> findBySkill(String skillName);

    List<Skill> findByLocation(String location);

}
