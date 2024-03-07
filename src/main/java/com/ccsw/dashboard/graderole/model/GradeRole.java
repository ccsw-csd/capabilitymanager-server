package com.ccsw.dashboard.graderole.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vista_grados_roles")
public class GradeRole {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
    @Column(name="vc_Grado")
    private String grade;
    
    @Column(name="vc_Profile_Rol_L1")
    private String role;
    
    @Column(name="id_Import_capacidades")
    private Long idImportCapacidades;
    
    @Column(name="id_Import_staffing")
    private Long idImportStaffing;
    
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdImportCapacidades() {
		return idImportCapacidades;
	}

	public void setIdImportCapacidades(Long idImportCapacidades) {
		this.idImportCapacidades = idImportCapacidades;
	}

	public Long getIdImportStaffing() {
		return idImportStaffing;
	}

	public void setIdImportStaffing(Long idImportStaffing) {
		this.idImportStaffing = idImportStaffing;
	}

	
		
}
