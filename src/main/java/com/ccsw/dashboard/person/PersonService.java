package com.ccsw.dashboard.person;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import com.ccsw.dashboard.person.model.Person;

public interface PersonService {

    // Person findById(Long id) throws EntityNotFoundException;
    List<Person> findAll();

    List<Person> findBySkill(String skillName);

    List<Person> findByLocation(String location);

}
