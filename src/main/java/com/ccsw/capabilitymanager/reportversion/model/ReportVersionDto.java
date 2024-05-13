package com.ccsw.capabilitymanager.reportversion.model;

import java.time.LocalDateTime;

import com.ccsw.capabilitymanager.roleversion.model.RoleVersionDto;
import com.ccsw.capabilitymanager.staffingversion.model.StaffingVersionDto;


public class ReportVersionDto {
	

	private Long id;
    private RoleVersionDto roleVersion;
    private StaffingVersionDto staffingVersion;
    private int screenshot;
    private LocalDateTime fechaImportacion;
    private String descripcion;
    private String usuario;
    private LocalDateTime fechaModificacion;
    private String comentarios;       
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	

	public RoleVersionDto getRoleVersion() {
		return roleVersion;
	}
	public void setRoleVersion(RoleVersionDto roleVersion) {
		this.roleVersion = roleVersion;
	}
	public StaffingVersionDto getStaffingVersion() {
		return staffingVersion;
	}
	public void setStaffingVersion(StaffingVersionDto staffingVersion) {
		this.staffingVersion = staffingVersion;
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
