package com.ccsw.capabilitymanager.itinerariosdataimport.model;

public class ItinerariosDataImportDto {

	private int id;

	private String GGID;

	private String tipoActividad;

	private String nombreActividad;

	private String codigoActividad;

	private String fechaInicio;

	private String fechaFinalizacion;

	private String porcentajeAvance;

	private String fechaUltimaActividad;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGGID() {
		return GGID;
	}

	public void setGGID(String gGID) {
		GGID = gGID;
	}

	public String getTipoActividad() {
		return tipoActividad;
	}

	public void setTipoActividad(String tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	public String getNombreActividad() {
		return nombreActividad;
	}

	public void setNombreActividad(String nombreActividad) {
		this.nombreActividad = nombreActividad;
	}

	public String getCodigoActividad() {
		return codigoActividad;
	}

	public void setCodigoActividad(String codigoActividad) {
		this.codigoActividad = codigoActividad;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFinalizacion() {
		return fechaFinalizacion;
	}

	public void setFechaFinalizacion(String fechaFinalizacion) {
		this.fechaFinalizacion = fechaFinalizacion;
	}

	public String getPorcentajeAvance() {
		return porcentajeAvance;
	}

	public void setPorcentajeAvance(String porcentajeAvance) {
		this.porcentajeAvance = porcentajeAvance;
	}

	public String getFechaUltimaActividad() {
		return fechaUltimaActividad;
	}

	public void setFechaUltimaActividad(String fechaUltimaActividad) {
		this.fechaUltimaActividad = fechaUltimaActividad;
	}
	


}
