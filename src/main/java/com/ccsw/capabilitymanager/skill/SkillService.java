package com.ccsw.capabilitymanager.skill;

import java.util.List;

import com.ccsw.capabilitymanager.skill.model.Skill;

public interface SkillService {

    // Skill findById(Long id) throws EntityNotFoundException;
    List<Skill> findAll();

    List<Skill> findBySkill(String skillName);

    List<Skill> findByLocation(String location);

}
