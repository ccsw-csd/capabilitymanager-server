package com.ccsw.dashboard.config.person;

import com.ccsw.dashboard.config.person.model.Person;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface PersonService {

    // Person findById(Long id) throws EntityNotFoundException;
    List<Person> findAll();
}
