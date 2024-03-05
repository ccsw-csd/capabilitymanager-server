package com.ccsw.dashboard.staffingversion.model;

import java.time.LocalDateTime;


public class StaffingVersionDto {
	

	private int id;
	private int idTipoInterfaz;
    private LocalDateTime fechaImportacion;
    private int numRegistros;
    private String nombreFichero;
    private String descripcion;
    private String usuario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getIdTipoInterfaz() {
		return idTipoInterfaz;
	}

	public void setIdTipoInterfaz(int idTipoInterfaz) {
		this.idTipoInterfaz = idTipoInterfaz;
	}

	public LocalDateTime getFechaImportacion() {
		return fechaImportacion;
	}

	public void setFechaImportacion(LocalDateTime fechaImportacion) {
		this.fechaImportacion = fechaImportacion;
	}	
}
