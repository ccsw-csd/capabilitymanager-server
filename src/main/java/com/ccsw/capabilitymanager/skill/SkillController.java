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

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<SkillDto> findAll(){
        return this.skillService.findAll().stream().map(g->mapper.map(g,SkillDto.class)).toList();
    }

    @RequestMapping(path = "/skill/{skill}", method = RequestMethod.GET)
    public List<Skill> findBySkill(@PathVariable String skill){
        List<Skill> people = this.skillService.findBySkill(skill);
        return people;
    }

    @RequestMapping(path = "/location", method = RequestMethod.GET)
    public List<Skill> findByLocation(@RequestParam String location){
        List<Skill> people = this.skillService.findByLocation(location);
        return people;
    }
}
