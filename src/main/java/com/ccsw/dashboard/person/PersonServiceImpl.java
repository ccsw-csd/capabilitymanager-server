package com.ccsw.dashboard.person;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.person.model.Person;

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

    @Override
    public List<Person> findBySkill(String skillName) {
        return (List<Person>) this.personRepository.findBySkillDescription(skillName);
    }

    @Override
    public List<Person> findByLocation(String location) {
        return (List<Person>) this.personRepository.findPersonByLocation(location);
    }
}
