package com.ccsw.capabilitymanager.itinerariosdataimport.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Itinerarios")
public class ItinerariosDataImport  implements Comparable<ItinerariosDataImport>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "GGID", nullable = false)
	private String GGID;

	@Column(name = "tipo_actividad", nullable = false)
	private String tipoActividad;

	@Column(name = "nombre_actividad")
	private String nombreActividad;

	@Column(name = "codigo_actividad")
	private String codigoActividad;

	@Column(name = "fecha_inicio")
	private String fechaInicio;
	
	@Column(name = "fecha_finalizacion")
	private String fechaFinalizacion;

	@Column(name = "porcentaje_avance")
	private String porcentajeAvance;

	@Column(name = "fecha_ultima_actividad")
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



	@Override
	public int compareTo(ItinerariosDataImport o) {
		
		return 0;
	}

	
}
