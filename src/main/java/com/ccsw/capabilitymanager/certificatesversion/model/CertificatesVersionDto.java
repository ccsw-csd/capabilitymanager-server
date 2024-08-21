package com.ccsw.capabilitymanager.certificatesversion.model;

import java.time.LocalDateTime;

public class CertificatesVersionDto {

	private Long id;
    private String idTipoInterfaz;
    private LocalDateTime fechaImportacion;
    private int numRegistros;
    private String nombreFichero;
    private String descripcion;
    private String usuario;
    
    
	public CertificatesVersionDto(Long id, String idTipoInterfaz, LocalDateTime fechaImportacion, int numRegistros,
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
	
	public CertificatesVersionDto() {
		super();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdTipoInterfaz() {
		return idTipoInterfaz;
	}
	public void setIdTipoInterfaz(String idTipoInterfaz) {
		this.idTipoInterfaz = idTipoInterfaz;
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
    
}
