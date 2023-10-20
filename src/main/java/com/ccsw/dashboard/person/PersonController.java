package com.ccsw.dashboard.person;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ccsw.dashboard.person.model.Person;
import com.ccsw.dashboard.person.model.PersonDto;

import java.util.List;


@RequestMapping(value = "/person")
@RestController
public class PersonController {

    @Autowired
    private PersonService personService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<PersonDto> findAll(){
        return this.personService.findAll().stream().map(g->mapper.map(g,PersonDto.class)).toList();
    }

    @RequestMapping(path = "/skill/{skill}", method = RequestMethod.GET)
    public List<Person> findBySkill(@PathVariable String skill){
        List<Person> people = this.personService.findBySkill(skill);
        return people;
    }

    @RequestMapping(path = "/location", method = RequestMethod.GET)
    public List<Person> findByLocation(@RequestParam String location){
        List<Person> people = this.personService.findByLocation(location);
        return people;
    }
}
