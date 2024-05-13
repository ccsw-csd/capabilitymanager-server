package com.ccsw.capabilitymanager.config.grade;

import com.ccsw.capabilitymanager.config.grade.model.Grade;

import java.util.List;

public interface GradeService {

	List<Grade> findAll();
	Grade findById(Long id);
	Grade findByGrade(String grade);   

}
