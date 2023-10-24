package com.ccsw.dashboard.grade_role.model;

import java.util.LinkedHashMap;
import java.util.List;
import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.role.model.Role;

public class GradeRoleAll {	
	
	private LinkedHashMap<String, LinkedHashMap<String, Long>> gradeRole;	
	private List<Grade> grades;
	private List<Role> roles;
	
	public LinkedHashMap<String, LinkedHashMap<String, Long>> getGradeRole() {
		return gradeRole;
	}
	public void setGradeRole(LinkedHashMap<String, LinkedHashMap<String, Long>> gradeRole) {
		this.gradeRole = gradeRole;
	}
	public List<Grade> getGrades() {
		return grades;
	}
	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}	
}
