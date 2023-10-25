package com.ccsw.dashboard.grade_role.model;

import java.util.List;

import com.ccsw.dashboard.config.grade.model.GradeDto;
import com.ccsw.dashboard.config.role.model.RoleDto;

public class GradeRoleAllDto {	
	
	private List<GradeRoleTotalDto> gradeRole;
	private List<GradeDto> grades;
	private List<RoleDto> roles;
	
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
	public List<GradeRoleTotalDto> getGradeRole() {
		return gradeRole;
	}
	public void setGradeRole(List<GradeRoleTotalDto> gradeRole) {
		this.gradeRole = gradeRole;
	}	
}
