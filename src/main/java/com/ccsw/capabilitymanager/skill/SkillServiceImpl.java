package com.ccsw.capabilitymanager.skill;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.skill.model.Skill;

import java.util.List;

@Service
@Transactional
public class SkillServiceImpl implements SkillService{

    @Autowired
    private SkillRepository skillRepository;


    public Skill findById(Long id) throws EntityNotFoundException {
        return this.skillRepository.findById(id).orElseThrow(() -> {
            CapabilityLogger.logError("Entidad con id: " + id + " no encontrada.");
            return new EntityNotFoundException();
        });
    }

    @Override
    public List<Skill> findAll() {
        return (List<Skill>) this.skillRepository.findAll();
    }

    @Override
    public List<Skill> findBySkill(String skillName) {
        return (List<Skill>) this.skillRepository.findBySkillDescription(skillName);
    }

    @Override
    public List<Skill> findByLocation(String location) {
        return (List<Skill>) this.skillRepository.findSkillByLocation(location);
    }
}
