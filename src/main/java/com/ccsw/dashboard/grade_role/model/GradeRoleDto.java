package com.ccsw.dashboard.grade_role.model;

import com.ccsw.dashboard.config.grade.model.GradeDto;
import com.ccsw.dashboard.config.role.model.RoleDto;

public class GradeRoleDto {	

    private GradeDto grade;
    private RoleDto role;
    private Long total;
	public GradeDto getGrade() {
		return grade;
	}
	public void setGrade(GradeDto grade) {
		this.grade = grade;
	}
	public RoleDto getRole() {
		return role;
	}
	public void setRole(RoleDto role) {
		this.role = role;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public GradeRoleDto(GradeDto grade, RoleDto role, Long total) {
		super();
		this.grade = grade;
		this.role = role;
		this.total = total;
	}	
}
