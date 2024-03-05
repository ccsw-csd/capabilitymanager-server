package com.ccsw.dashboard.reportversion.model;

import java.time.LocalDateTime;


public class ReportVersionDto {
	

	private int id;
    private int idVersionCapacidades;
    private int idVersionStaffing;
    private int screenshot;
    private LocalDateTime fechaImportacion;
    private String descripcion;
    private String usuario;
    private LocalDateTime fechaModificacion;
    private String comentarios;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdVersionCapacidades() {
		return idVersionCapacidades;
	}
	public void setIdVersionCapacidades(int idVersionCapacidades) {
		this.idVersionCapacidades = idVersionCapacidades;
	}
	public int getIdVersionStaffing() {
		return idVersionStaffing;
	}
	public void setIdVersionStaffing(int idVersionStaffing) {
		this.idVersionStaffing = idVersionStaffing;
	}
	public int getScreenshot() {
		return screenshot;
	}
	public void setScreenshot(int screenshot) {
		this.screenshot = screenshot;
	}
	public LocalDateTime getFechaImportacion() {
		return fechaImportacion;
	}
	public void setFechaImportacion(LocalDateTime fechaImportacion) {
		this.fechaImportacion = fechaImportacion;
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
	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
}
