package com.ccsw.dashboard.profile.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tmp_export_formdata") //tmp_export_formdata  tmp_prod_formdata
public class Profile {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;    
    
    @Column(name="vc_Profile_Staffing_GGID")
    private String ggid;
    
    @Column(name="vc_Profile_SAGA")
    private String saga;
    
    @Column(name="vc_Profile_Staffing_Practica")
    private String practica;
    
    @Column(name="vc_Profile_Staffing_Grado")
    private String grado;
    
    @Column(name="vc_Profile_Staffing_Categoria")
    private String categoria;
    
    @Column(name="vc_Profile_Staffing_Centro")
    private String centro;
    
    @Column(name="vc_Profile_Nombre_Completo")
    private String nombre;
    
    @Column(name="vc_Profile_Email")
    private String email;
    
    @Column(name="vc_Profile_Staffing_Localizacion")
    private String localizacion;
    
    @Column(name="vc_Profile_Staffing_Status")
    private String status;
    
    @Column(name="vc_Profile_Staffing_Perfil_Tecnico")
 	private String perfilStaffing;   
    
    @Column(name="vc_Profile_Rol_Actual")
    private String actual;
    
    @Column(name="vc_Profile_Rol_Perfil")
    private String perfil;
    
    @Column(name="vc_Profile_Rol_Perfil_Experiencia")
    private String experiencia;
    
    @Column(name="vc_Profile_Rol_Perfil_Tecnico")
    private String tecnico;
       
    @Column(name="vc_Profile_Skill_Cloud_Native")
    private String skillCloudNative;
    
    @Column(name="vc_Profile_Skill_Cloud_Native_Experiencia")
    private String skillCloudNativeExperiencia;
    
    @Column(name="vc_Profile_Skill_Low_Code")
    private String skillLowCode;
    
    @Column(name="vc_Profile_Sector_Experiencia")
    private String sectorExperiencia;

	public String getGgid() {
		return ggid;
	}

	public void setGgid(String ggid) {
		this.ggid = ggid;
	}

	public String getSaga() {
		return saga;
	}

	public void setSaga(String saga) {
		this.saga = saga;
	}

	public String getPractica() {
		return practica;
	}

	public void setPractica(String practica) {
		this.practica = practica;
	}

	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCentro() {
		return centro;
	}

	public void setCentro(String centro) {
		this.centro = centro;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPerfilStaffing() {
		return perfilStaffing;
	}

	public void setPerfilStaffing(String perfilStaffing) {
		this.perfilStaffing = perfilStaffing;
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

	public String getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(String experiencia) {
		this.experiencia = experiencia;
	}

	public String getTecnico() {
		return tecnico;
	}

	public void setTecnico(String tecnico) {
		this.tecnico = tecnico;
	}

	public String getSkillCloudNative() {
		return skillCloudNative;
	}

	public void setSkillCloudNative(String skillCloudNative) {
		this.skillCloudNative = skillCloudNative;
	}

	public String getSkillCloudNativeExperiencia() {
		return skillCloudNativeExperiencia;
	}

	public void setSkillCloudNativeExperiencia(String skillCloudNativeExperiencia) {
		this.skillCloudNativeExperiencia = skillCloudNativeExperiencia;
	}

	public String getSkillLowCode() {
		return skillLowCode;
	}

	public void setSkillLowCode(String skillLowCode) {
		this.skillLowCode = skillLowCode;
	}

	public String getSectorExperiencia() {
		return sectorExperiencia;
	}

	public void setSectorExperiencia(String sectorExperiencia) {
		this.sectorExperiencia = sectorExperiencia;
	}
	
}
