package com.ccsw.capabilitymanager.versioncapacidades.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "version_capacidades")
public class VersionCapacidades {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "id_Tipo_interfaz", nullable = false)
	private String idTipointerfaz;

	@Column(name = "fecha_Importacion", nullable = false)
	private LocalDateTime fechaImportacion;
	

	@Column(name = "num_Registros", nullable = false)
	private int numRegistros;

	@Column(name = "nombre_Fichero", nullable = false)
	private String nombreFichero;

	@Column(name = "descripcion")
	private String descripcion;

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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