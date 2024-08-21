package com.ccsw.capabilitymanager.roleversion.model;

import java.time.LocalDateTime;

public class RoleVersionDto {
	

	private Long id;
    private String idTipoInterfaz;
    private LocalDateTime fechaImportacion;
    private int numRegistros;
    private String nombreFichero;
    private String descripcion;
    private String usuario;
    
    
   
	public RoleVersionDto(Long id, String idTipoInterfaz, LocalDateTime fechaImportacion, int numRegistros,
			String nombreFichero, String descripcion, String usuario) {
		super();
		this.id = id;
		this.idTipoInterfaz = idTipoInterfaz;
		this.fechaImportacion = fechaImportacion;
		this.numRegistros = numRegistros;
		this.nombreFichero = nombreFichero;
		this.descripcion = descripcion;
		this.usuario = usuario;
	}
	
	

	public RoleVersionDto() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getIdTipoInterfaz() {
		return idTipoInterfaz;
	}

	public void setIdTipoInterfaz(String idTipoInterfaz) {
		this.idTipoInterfaz = idTipoInterfaz;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}	
}
