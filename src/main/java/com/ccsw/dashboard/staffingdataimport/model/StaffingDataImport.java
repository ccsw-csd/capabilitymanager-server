package com.ccsw.dashboard.staffingdataimport.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dm_staffing_import")
public class StaffingDataImport implements Comparable<StaffingDataImport> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "num_import_codeid", nullable = false)
	private int numImportCode;

	@Column(name = "vc_Profile_SAGA", nullable = false)
	private String vcProfileSAGA;

	@Column(name = "vc_Profile_GGID", nullable = false)
	private String vcProfileGGID;

	@Column(name = "vc_Profile_Practica", nullable = false)
	private String vcProfilePractica;

	@Column(name = "vc_Profile_Grado", nullable = false)
	private String vcProfileGrado;

	@Column(name = "vc_Profile_Categoria")
	private String vcProfileCategoria;

	@Column(name = "vc_Profile_Centro", nullable = false)
	private String vcProfileCentro;

	@Column(name = "vc_Profile_Nombre", nullable = false)
	private String vcProfileNombre;

	@Column(name = "vc_Profile_Apellidos")
	private String vcProfileApellidos;

	@Column(name = "vc_Profile_Localizacion")
	private String vcProfileLocalizacion;

	@Column(name = "vc_Profile_Perfil_Tecnico")
	private String vcProfilePerfilTecnico;

	@Column(name = "vc_Profile_Status")
	private String vcProfileStatus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumImportCodeID() {
		return numImportCode;
	}

	public void setNumImportCodeID(int numImportCodeID) {
		this.numImportCode = numImportCodeID;
	}

	public String getVcProfileSAGA() {
		return vcProfileSAGA;
	}

	public void setVcProfileSAGA(String vcProfileSAGA) {
		this.vcProfileSAGA = vcProfileSAGA;
	}

	public String getVcProfileGGID() {
		return vcProfileGGID;
	}

	public void setVcProfileGGID(String vcProfileGGID) {
		this.vcProfileGGID = vcProfileGGID;
	}

	public String getVcProfilePractica() {
		return vcProfilePractica;
	}

	public void setVcProfilePractica(String vcProfilePractica) {
		this.vcProfilePractica = vcProfilePractica;
	}

	public String getVcProfileGrado() {
		return vcProfileGrado;
	}

	public void setVcProfileGrado(String vcProfileGrado) {
		this.vcProfileGrado = vcProfileGrado;
	}

	public String getVcProfileCategoria() {
		return vcProfileCategoria;
	}

	public void setVcProfileCategoria(String vcProfileCategoria) {
		this.vcProfileCategoria = vcProfileCategoria;
	}

	public String getVcProfileCentro() {
		return vcProfileCentro;
	}

	public void setVcProfileCentro(String vcProfileCentro) {
		this.vcProfileCentro = vcProfileCentro;
	}

	public String getVcProfileNombre() {
		return vcProfileNombre;
	}

	public void setVcProfileNombre(String vcProfileNombre) {
		this.vcProfileNombre = vcProfileNombre;
	}

	public String getVcProfileApellidos() {
		return vcProfileApellidos;
	}

	public void setVcProfileApellidos(String vcProfileApellidos) {
		this.vcProfileApellidos = vcProfileApellidos;
	}

	public String getVcProfileLocalizacion() {
		return vcProfileLocalizacion;
	}

	public void setVcProfileLocalizacion(String vcProfileLocalizacion) {
		this.vcProfileLocalizacion = vcProfileLocalizacion;
	}

	public String getVcProfilePerfilTecnico() {
		return vcProfilePerfilTecnico;
	}

	public void setVcProfilePerfilTecnico(String vcProfilePerfilTecnico) {
		this.vcProfilePerfilTecnico = vcProfilePerfilTecnico;
	}

	public String getVcProfileStatus() {
		return vcProfileStatus;
	}

	public void setVcProfileStatus(String vcProfileStatus) {
		this.vcProfileStatus = vcProfileStatus;
	}

	@Override
	public int compareTo(StaffingDataImport o) {
		// TODO Auto-generated method stub
		return Integer.compare(this.getId(), o.getId());
	}

}
