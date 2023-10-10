package com.ccsw.dashboard.config.person;

import com.ccsw.dashboard.config.person.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping(value = "/person")
@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Person> findAll(){
        List<Person> people = this.personService.findAll();
        return people;
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
