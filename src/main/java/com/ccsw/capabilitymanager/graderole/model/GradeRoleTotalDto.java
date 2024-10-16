package com.ccsw.capabilitymanager.graderole.model;

import com.ccsw.capabilitymanager.config.grade.model.GradeDto;
import com.ccsw.capabilitymanager.config.role.model.RoleDto;

public class GradeRoleTotalDto {	

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
	public GradeRoleTotalDto(GradeDto grade, RoleDto role, Long total) {
		super();
		this.grade = grade;
		this.role = role;
		this.total = total;
	}	
}
