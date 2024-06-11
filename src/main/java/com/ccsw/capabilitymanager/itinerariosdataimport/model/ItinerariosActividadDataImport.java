package com.ccsw.capabilitymanager.itinerariosdataimport.model;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "actividad")
public class ItinerariosActividadDataImport  implements Comparable<ItinerariosActividadDataImport>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "GGID")
	private String GGID;
	
	@Column(name = "codigo_actividad", nullable = false)
	private String pathwayId;

	@Column(name = "nombre_actividad", nullable = false)
	private String pathwayTitle;
	
	@Column(name = "porcentaje_avance")
	private Double completionPercent;

	@Column(name = "fecha_inicio")
	private Date enrollmentDate;
	
	@Column(name = "estado")
	private String estado;
	
	@Column(name = "fecha_ultima_actividad")
	private Date recentActivityDate;

	@Column(name = "fecha_finalizacion")
	private Date completedDate;
	
	@Column(name = "tipo_actividad_id")
	private Integer typeActivity;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGGID() {
		return GGID;
	}

	public void setGGID(String gGID) {
		GGID = gGID;
	}

	public String getPathwayId() {
		return pathwayId;
	}

	public void setPathwayId(String pathwayId) {
		this.pathwayId = pathwayId;
	}

	public String getPathwayTitle() {
		return pathwayTitle;
	}

	public void setPathwayTitle(String pathwayTitle) {
		this.pathwayTitle = pathwayTitle;
	}

	public Double getCompletionPercent() {
		return completionPercent;
	}

	public void setCompletionPercent(Double completionPercent) {
		this.completionPercent = completionPercent;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public Date getRecentActivityDate() {
		return recentActivityDate;
	}

	public void setRecentActivityDate(Date recentActivityDate) {
		this.recentActivityDate = recentActivityDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public Integer getTypeActivity() {
		return typeActivity;
	}

	public void setTypeActivity(Integer typeActivity) {
		this.typeActivity = typeActivity;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public int compareTo(ItinerariosActividadDataImport o) {
		
		return 0;
	}






	
}
