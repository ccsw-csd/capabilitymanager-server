package com.ccsw.dashboard.grade_role.model;

import java.util.List;

import com.ccsw.dashboard.config.grade.model.Grade;
import com.ccsw.dashboard.config.role.model.Role;

public class GradeRoleAll {	
	
	private List<GradeRoleTotal> gradeRoleTotal;	
	private List<Grade> grades;
	private List<Role> roles;
	public List<GradeRoleTotal> getGradeRoleTotal() {
		return gradeRoleTotal;
	}
	public void setGradeRoleTotal(List<GradeRoleTotal> gradeRoleTotal) {
		this.gradeRoleTotal = gradeRoleTotal;
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
