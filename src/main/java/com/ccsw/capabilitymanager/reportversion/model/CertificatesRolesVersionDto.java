package com.ccsw.capabilitymanager.reportversion.model;

import java.util.Date;

public class CertificatesRolesVersionDto {

	private Long id;
	private int SAGA;
	private Date fechaCarga;
	private String usuario;
	private String rolFormulario;
	private String Certificado;
	private int idVersion;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSAGA() {
		return SAGA;
	}

	public void setSAGA(int sAGA) {
		SAGA = sAGA;
	}

	public Date getFechaCarga() {
		return fechaCarga;
	}

	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getRolFormulario() {
		return rolFormulario;
	}

	public void setRolFormulario(String rolFormulario) {
		this.rolFormulario = rolFormulario;
	}

	public String getCertificado() {
		return Certificado;
	}

	public void setCertificado(String certificado) {
		Certificado = certificado;
	}

	public int getIdVersion() {
		return idVersion;
	}

	public void setIdVersion(int idVersion) {
		this.idVersion = idVersion;
	}
	
	

}
