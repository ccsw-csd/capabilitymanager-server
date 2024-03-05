package com.ccsw.dashboard.staffingdataimport.model;

import jakarta.persistence.Column;

@SuppressWarnings("unused")
public class StaffingDataImportDto {

	private int id;
	private int numImportCodeId;
	private String vcProfileSAGA;
	private String vcProfileGGID;
	private String vcProfilePractica;
	private String vcProfileGrado;
	private String vcProfileCategoria;
	private String vcProfileCentro;
	private String vcProfileNombre;
	private String vcProfileApellidos;
	private String vcProfileLocalizacion;
	private String vcProfilePerfilTecnico;
	private String vcProfileStatus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumImportCodeId() {
		return numImportCodeId;
	}

	public void setNumImportCodeId(int numImportCodeId) {
		this.numImportCodeId = numImportCodeId;
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

}
