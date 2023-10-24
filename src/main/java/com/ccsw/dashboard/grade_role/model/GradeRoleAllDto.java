package com.ccsw.dashboard.grade_role.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.grade.model.GradeDto;
import com.ccsw.dashboard.config.role.model.Role;
import com.ccsw.dashboard.config.role.model.RoleDto;

public class GradeRoleAllDto {	
	
	//private LinkedHashMap<GradeDto, LinkedHashMap<RoleDto, Long>> gradeRole;
	private List<GradeRoleDto> gradeRole;
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
	public List<GradeRoleDto> getGradeRole() {
		return gradeRole;
	}
	public void setGradeRole(List<GradeRoleDto> gradeRole) {
		this.gradeRole = gradeRole;
	}	
}
