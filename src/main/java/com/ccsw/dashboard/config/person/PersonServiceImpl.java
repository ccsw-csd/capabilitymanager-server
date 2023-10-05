package com.ccsw.dashboard.config.person;

import com.ccsw.dashboard.config.person.model.Person;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements PersonService{

    @Autowired
    private PersonRepository personRepository;


    public Person findById(Long id) throws EntityNotFoundException {
        return this.personRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Person> findAll() {
        return (List<Person>) this.personRepository.findAll();
    }
}
