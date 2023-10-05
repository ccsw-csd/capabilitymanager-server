package com.ccsw.dashboard.config.person;

import com.ccsw.dashboard.config.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends CrudRepository<Person, Long>, JpaRepository<Person, Long> {

    /**
     * Get All Person from BBDD
     */



}
