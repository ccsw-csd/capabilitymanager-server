package com.ccsw.dashboard.grade_role.model;

public class GradeRoleTotal {	

    private String grade;
    private String role;
    private Long total;

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public GradeRoleTotal(String grade, String role, Long total) {
		super();
		this.grade = grade;
		this.role = role;
		this.total = total;
	}	
}
