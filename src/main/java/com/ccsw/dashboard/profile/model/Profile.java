package com.ccsw.dashboard.profile.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tmp_prod_formdata")
public class Profile {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
    @Column(name="vc_Profile_Rol_Actual")
    private String actual;
    
    @Column(name="vc_Profile_Rol_Perfil")
    private String perfil;
    
    @Column(name="vc_Profile_Rol_Perfil_Tecnico")
    private String tecnico;
    
    @Column(name="vc_Profile_Rol_Perfil_Experiencia")
    private String experiencia;
    
    @Column(name="vc_Profile_Sector_Experiencia")
    private String sectorExperiencia;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getTecnico() {
		return tecnico;
	}

	public void setTecnico(String tecnico) {
		this.tecnico = tecnico;
	}

	public String getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(String experiencia) {
		this.experiencia = experiencia;
	}

	public String getSectorExperiencia() {
		return sectorExperiencia;
	}

	public void setSectorExperiencia(String sectorExperiencia) {
		this.sectorExperiencia = sectorExperiencia;
	}    
			
}
