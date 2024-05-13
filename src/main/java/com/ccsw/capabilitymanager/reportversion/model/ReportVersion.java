package com.ccsw.capabilitymanager.reportversion.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "informe_modelo_capacidades")
public class ReportVersion  implements Comparable<ReportVersion>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="id_Version_capacidades", nullable = false)
    private int idVersionCapacidades;
	
	@Column(name="id_Version_staffing", nullable = false)
    private int idVersionStaffing;
	
	@Column(name="screenshot", nullable = false)
    private int screenshot;
	
    @Column(name="fecha_Importacion", nullable = true)
    private LocalDateTime fechaImportacion;

    @Column(name="descripcion", nullable = true)
    private String descripcion;
    
    @Column(name="usuario", nullable = true)
    private String usuario;
    
    @Column(name="fecha_Modificacion", nullable = true)
    private LocalDateTime fechaModificacion;
    
    @Column(name="comentarios", nullable = true)
    private String comentarios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	@Override
	public int compareTo(ReportVersion o) {
		return Long.valueOf(o.getId()).compareTo(this.id);
	}
}
