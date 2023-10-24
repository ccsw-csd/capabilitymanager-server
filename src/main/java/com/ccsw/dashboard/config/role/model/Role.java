package com.ccsw.dashboard.config.role.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aux_roles")
public class Role implements Comparable<Role>{
		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

    @Column(name="vc_Roles", nullable = false)
    private String role;
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public int compareTo(Role r) {
		return role.compareToIgnoreCase(r.getRole());
	}
}
