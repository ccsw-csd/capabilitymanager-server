package com.ccsw.capabilitymanager.reportversion.model;

import java.time.LocalDateTime;


public class GenerateReportVersionDto {
    private int idRoleVersion;
    private int idStaffingVersion;
    private int idVersionCertificaciones;
    private int screenshot = 0;
    private LocalDateTime fechaImportacion;
    private String description;
    private String user;
    private LocalDateTime fechaModificacion;
    private String comments;
    
	public int getIdRoleVersion() {
		return idRoleVersion;
	}
	public void setIdRoleVersion(int ifRoleVersion) {
		this.idRoleVersion = ifRoleVersion;
	}
	public int getIdStaffingVersion() {
		return idStaffingVersion;
	}
	public void setIdStaffingVersion(int idStaffingVersion) {
		this.idStaffingVersion = idStaffingVersion;
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
	public String getDescription() {
		return description;
	}
	public void setFescription(String description) {
		this.description = description;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String usuario) {
		this.user = usuario;
	}
	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getIdVersionCertificaciones() {
		return idVersionCertificaciones;
	}
	public void setIdVersionCertificaciones(int idVersionCertificaciones) {
		this.idVersionCertificaciones = idVersionCertificaciones;
	}
	public void setDescription(String description) {
		this.description = description;
	}  
    
    
}
