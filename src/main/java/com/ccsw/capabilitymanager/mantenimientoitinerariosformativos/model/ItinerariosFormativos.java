package com.ccsw.capabilitymanager.mantenimientoitinerariosformativos.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mantenimiento_itinerarios_formativos")
public class ItinerariosFormativos implements Comparable<ItinerariosFormativos>{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false)
	private Long id;
	@Column(name="Codigo", nullable = false, unique = true)
	private String codigo;
	@Column(name="Nombre", nullable = false)
	private String name;
	@Column(name="Fecha_Alta")
	private Date fecha_Alta;
	@Column(name="Fecha_Baja")
	private Date fecha_Baja;
	@Column(name="Fecha_Modif")
	private Date fecha_Modif;
	@Column(name="Usuario")
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
	@Override
	public int compareTo(ItinerariosFormativos o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
