package com.ccsw.dashboard.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ccsw.dashboard.person.model.Person;

import java.util.List;


@Repository
public interface PersonRepository extends CrudRepository<Person, Long>, JpaRepository<Person, Long> {

    List<Person> findBySkillDescription(String skill);
    List<Person> findPersonByLocation(String location);


}
