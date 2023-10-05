package com.ccsw.dashboard.config.person;

import com.ccsw.dashboard.config.person.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
