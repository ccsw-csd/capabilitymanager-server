package com.ccsw.capabilitymanager.config.grade.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aux_grados")
public class Grade  implements Comparable<Grade>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    @Column(name="vc_grado", nullable = false)
    private String grade;
    
    @Column(name="vc_ord", nullable = false)
    private int ord;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

//	@Override
//	public int compareTo(Grade g) {
//		return grade.compareToIgnoreCase(g.getGrade());
//	}
	
	@Override	
	public int compareTo(Grade o) {
		if (this.ord != o.getOrd())
			return Integer.valueOf(this.ord).compareTo(o.getOrd());
		return this.grade.compareTo(o.getGrade());
	}	
}
