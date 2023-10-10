package com.ccsw.dashboard.config.person;

import com.ccsw.dashboard.config.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PersonRepository extends CrudRepository<Person, Long>, JpaRepository<Person, Long> {

    List<Person> findBySkillDescription(String skill);
    List<Person> findPersonByLocation(String location);


}
