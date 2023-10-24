package com.ccsw.dashboard.grade_role.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ccsw.dashboard.config.grade.model.GradeDto;
import com.ccsw.dashboard.config.role.model.RoleDto;

public class GradeRoleAll {	
	
	private LinkedHashMap<String, LinkedHashMap<String, Long>> gradeRole;
	private List<GradeDto> grades;
	private List<RoleDto> roles;
	
	public LinkedHashMap<String, LinkedHashMap<String, Long>> getGradeRole() {
		return gradeRole;
	}
	public void setGradeRole(LinkedHashMap<String, LinkedHashMap<String, Long>> gradeRole) {
		this.gradeRole = gradeRole;
	}
	public List<GradeDto> getGrades() {
		return grades;
	}
	public void setGrades(List<GradeDto> grades) {
		this.grades = grades;
	}
	public List<RoleDto> getRoles() {
		return roles;
	}
	public void setRoles(List<RoleDto> roles) {
		this.roles = roles;
	}
	
	
	
	
}
