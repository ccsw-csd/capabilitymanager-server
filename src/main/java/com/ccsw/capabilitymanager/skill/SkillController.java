package com.ccsw.capabilitymanager.skill;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ccsw.capabilitymanager.skill.model.Skill;
import com.ccsw.capabilitymanager.skill.model.SkillDto;

import java.util.List;


@RequestMapping(value = "/skill")
@RestController
public class SkillController {

    @Autowired
    private SkillService skillService;
    
    @Autowired
    DozerBeanMapper mapper;

    /**
     * Retrieves a list of all skills.
     * 
     * @return A list of {@link SkillDto} objects representing all skills.
     * 
     * <p>This method fetches all skills from the service and maps them to {@link SkillDto} objects before returning the list.</p>
     */
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<SkillDto> findAll(){
        return this.skillService.findAll().stream().map(g->mapper.map(g,SkillDto.class)).toList();
    }

    /**
     * Retrieves a list of skills by a specific skill name.
     * 
     * @param skill The name of the skill to search for.
     * @return A list of {@link Skill} objects matching the specified skill name.
     * 
     * <p>This method fetches skills from the service that match the provided skill name.</p>
     */
    @RequestMapping(path = "/skill/{skill}", method = RequestMethod.GET)
    public List<Skill> findBySkill(@PathVariable String skill){
        List<Skill> people = this.skillService.findBySkill(skill);
        return people;
    }

    /**
     * Retrieves a list of skills by location.
     * 
     * @param location The location to search for skills.
     * @return A list of {@link Skill} objects matching the specified location.
     * 
     * <p>This method fetches skills from the service that match the provided location.</p>
     */
    @RequestMapping(path = "/location", method = RequestMethod.GET)
    public List<Skill> findByLocation(@RequestParam String location){
        List<Skill> people = this.skillService.findByLocation(location);
        return people;
    }
}
