package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model;

import java.util.Date;

public class ItinerariosFormativosDto {

	
	private Long id;
	private String codigo;
	private String name;
	private Date fecha_Alta;
	private Date fecha_Baja;
	private Date fecha_Modif;
	private String usuario;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFecha_Alta() {
		return fecha_Alta;
	}
	public void setFecha_Alta(Date fecha_Alta) {
		this.fecha_Alta = fecha_Alta;
	}
	public Date getFecha_Baja() {
		return fecha_Baja;
	}
	public void setFecha_Baja(Date fecha_Baja) {
		this.fecha_Baja = fecha_Baja;
	}
	public Date getFecha_Modif() {
		return fecha_Modif;
	}
	public void setFecha_Modif(Date fecha_Modif) {
		this.fecha_Modif = fecha_Modif;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	
	
}
