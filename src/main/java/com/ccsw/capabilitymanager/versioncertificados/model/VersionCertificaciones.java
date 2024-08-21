package com.ccsw.capabilitymanager.versioncertificados.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "version_certificados")
public class VersionCertificaciones {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "id_tipo_interfaz", nullable = false)
	private String idTipointerfaz;
	
	@Column(name = "fecha_importacion", nullable = false)
	private LocalDateTime fechaImportacion;
	
	@Column(name = "num_registros", nullable = false)
	private int numRegistros;

	@Column(name = "nombre_fichero", nullable = false)
	private String nombreFichero;

	@Column(name = "description")
	private String description;

	@Column(name = "usuario")
	private String usuario;
	
	
	@Column(name = "fichero")
	private String fichero;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdTipointerfaz() {
		return idTipointerfaz;
	}

	public void setIdTipointerfaz(String idTipointerfaz) {
		this.idTipointerfaz = idTipointerfaz;
	}

	public LocalDateTime getFechaImportacion() {
		return fechaImportacion;
	}

	public void setFechaImportacion(LocalDateTime fechaImportacion) {
		this.fechaImportacion = fechaImportacion;
	}

	public int getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	public String getNombreFichero() {
		return nombreFichero;
	}

	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getFichero() {
		return fichero;
	}

	public void setFichero(String fichero) {
		this.fichero = fichero;
	}

}