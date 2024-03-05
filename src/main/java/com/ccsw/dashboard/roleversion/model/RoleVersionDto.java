package com.ccsw.dashboard.roleversion.model;

import java.time.LocalDateTime;

public class RoleVersionDto {
	

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

	public String getComentarios() {
		return usuario;
	}

	public void setComentarios(String comentarios) {
		this.usuario = comentarios;
	}

	public int getIdTipoInterfaz() {
		return idTipoInterfaz;
	}

	public void setIdTipoInterfaz(int idTipoInterfaz) {
		this.idTipoInterfaz = idTipoInterfaz;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}	
}
