package com.ccsw.dashboard.grade_role.model;

import java.util.List;
import java.util.Map;

import com.ccsw.dashboard.config.grade.model.GradeDto;
import com.ccsw.dashboard.config.role.model.RoleDto;

public class GradeRoleAll {	
	
	private Map<String, Map<String, Long>> gradeRole;
	private List<GradeDto> grades;
	private List<RoleDto> roles;
	
	
	public Map<String, Map<String, Long>> getGradeRole() {
		return gradeRole;
	}
	public void setGradeRole(Map<String, Map<String, Long>> gradeRole) {
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
