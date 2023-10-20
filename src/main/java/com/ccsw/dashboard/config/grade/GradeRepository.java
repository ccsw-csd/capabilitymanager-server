package com.ccsw.dashboard.config.grade;

import com.ccsw.dashboard.config.grade.model.Grade;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface GradeRepository extends CrudRepository<Grade, Long>, JpaRepository<Grade, Long> {

	Grade findByGrade(String grade);
	Optional<Grade> findById(Long id);
}
