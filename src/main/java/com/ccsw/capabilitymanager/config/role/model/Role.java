package com.ccsw.capabilitymanager.config.role.model;

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
	private Long id;

    @Column(name="vc_rol", nullable = false)
    private String role;
    
    @Column(name="vc_ord", nullable = false)
    private int ord;
    

    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}
	
//	@Override
//	public int compareTo(Role r) {
//		return role.compareToIgnoreCase(r.getRole());
//	}
	
	@Override	
	public int compareTo(Role o) {
		if (this.ord != o.getOrd())
			return Integer.valueOf(this.ord).compareTo(o.getOrd());
		return this.role.compareTo(o.getRole());
	}
}
