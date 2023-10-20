package com.ccsw.dashboard.config.grade.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aux_grados")
public class Grade {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

    @Column(name="vc_Grado", nullable = false)
    private String grade;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}	
		
}
