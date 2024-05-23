package com.ccsw.capabilitymanager.activity.model;

import java.math.BigDecimal;
import java.util.Date;


public class ActivityDTO {

    private Long id;
    private Long tipoActividadId;
    private String nombreActividad;
    private String estado;
    private Date fechaUltimaActividad;
    private Date fechaInicio;
    private Date fechaFinalizacion;
    private BigDecimal porcentajeAvance;
    private String observaciones;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
	public Long getTipoActividadId() {
		return tipoActividadId;
	}
	public void setTipoActividadId(Long tipoActividadId) {
		this.tipoActividadId = tipoActividadId;
	}
	public String getNombreActividad() {
		return nombreActividad;
	}
	public void setNombreActividad(String nombreActividad) {
		this.nombreActividad = nombreActividad;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Date getFechaUltimaActividad() {
		return fechaUltimaActividad;
	}
	public void setFechaUltimaActividad(Date fechaUltimaActividad) {
		this.fechaUltimaActividad = fechaUltimaActividad;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFinalizacion() {
		return fechaFinalizacion;
	}
	public void setFechaFinalizacion(Date fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}
	public BigDecimal getPorcentajeAvance() {
		return porcentajeAvance;
	}
	public void setPorcentajeAvance(BigDecimal porcentajeAvance) {
		this.porcentajeAvance = porcentajeAvance;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
