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
	private static final String ERROR_INIT = ">>> [ERROR][SkillServiceImpl] (";
    @Autowired
    private SkillRepository skillRepository;


    /**
     * Retrieves a skill by its ID.
     * 
     * @param id The ID of the skill to retrieve.
     * @return The {@link Skill} object with the specified ID.
     * @throws EntityNotFoundException If no skill with the given ID is found.
     * 
     * <p>This method fetches a skill from the repository based on the provided ID. 
     * If the skill is not found, it logs an error and throws an {@link EntityNotFoundException}.</p>
     */
    public Skill findById(Long id) throws EntityNotFoundException {
        return this.skillRepository.findById(id).orElseThrow(() -> {
            CapabilityLogger.logError(ERROR_INIT+ "findById) : Entidad con id: " + id + " no encontrada.");
            return new EntityNotFoundException();
        });
    }

    /**
     * Retrieves all skills.
     * 
     * @return A list of all {@link Skill} objects.
     * 
     * <p>This method fetches all skills from the repository and returns them as a list.</p>
     */
    @Override
    public List<Skill> findAll() {
        return (List<Skill>) this.skillRepository.findAll();
    }

    /**
     * Retrieves skills by their description.
     * 
     * @param skillName The description of the skills to retrieve.
     * @return A list of {@link Skill} objects that match the specified description.
     * 
     * <p>This method fetches skills from the repository based on the provided description.</p>
     */
    @Override
    public List<Skill> findBySkill(String skillName) {
        return (List<Skill>) this.skillRepository.findBySkillDescription(skillName);
    }

    /**
     * Retrieves skills by their location.
     * 
     * @param location The location of the skills to retrieve.
     * @return A list of {@link Skill} objects that match the specified location.
     * 
     * <p>This method fetches skills from the repository based on the provided location.</p>
     */
    @Override
    public List<Skill> findByLocation(String location) {
        return (List<Skill>) this.skillRepository.findSkillByLocation(location);
    }
}
