package com.ccsw.dashboard.grade_role.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vista_grados_roles")
public class GradeRole {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
    @Column(name="vc_Grado")
    private String grade;
    
    @Column(name="vc_Profile_Rol_L1")
    private String role;
    
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

		
}
