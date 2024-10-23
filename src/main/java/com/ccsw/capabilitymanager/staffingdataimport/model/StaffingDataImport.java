package com.ccsw.capabilitymanager.staffingdataimport.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "staffing")
public class StaffingDataImport implements Comparable<StaffingDataImport> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "num_import_code_id", nullable = false)
	private int numImportCode;

	@Column(name = "SAGA", nullable = false)
	private String saga;

	@Column(name = "GGID", nullable = false)
	private String ggid;

	@Column(name = "centro", nullable = false)
	private String centro;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "apellidos")
	private String apellidos;

	@Column(name = "localizacion")
	private String localizacion;

	@Column(name = "practica", nullable = false)
	private String practica;

	@Column(name = "grado", nullable = false)
	private String grado;

	@Column(name = "categoria")
	private String categoria;

	@Column(name = "perfil_tecnico")
	private String perfilTecnico;

	@Column(name = "primary_skill")
	private String primarySkill;

	@Column(name = "fecha_incorporacion")
	private Date fechaIncorporacion;
	
	@Column(name = "asignacion")
	private Integer asignacion;
	
	@Column(name = "status")
	private String status;

	@Column(name = "cliente_actual")
	private String clienteActual;
	
	@Column(name = "fecha_inicio_asignacion")
	private Date fechaInicioAsignacion;
	
	@Column(name = "fecha_fin_asignacion")
	private Date fechaFinAsignacion;

	@Column(name = "fecha_disponibilidad")
	private Date fechaDisponibilidad;

	@Column(name = "posicion_proyecto_futuro")
	private String posionProyectoFuturo;
	
	@Column(name = "colaboraciones")
	private String colaboraciones;
	
	@Column(name = "proyecto_anterior")
	private String proyectoAnterior;
	
	@Column(name = "ingles_escrito")
	private String inglesEscrito;

	@Column(name = "ingles_hablado")
	private String inglesHablado;

	@Column(name = "jornada")
	private String jornada;

	@Column(name = "meses_bench")
	private String mesesBench;

	@Column(name = "practice_area")
	private String practiceArea;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumImportCode() {
		return numImportCode;
	}

	public void setNumImportCode(int numImportCodeID) {
		this.numImportCode = numImportCodeID;
	}

	public String getSaga() {
		return saga;
	}

	public void setSaga(String saga) {
		this.saga = saga;
	}

	public String getGgid() {
		return ggid;
	}

	public void setGgid(String ggid) {
		this.ggid = ggid;
	}

	public String getCentro() {
		return centro;
	}

	public void setCentro(String centro) {
		this.centro = centro;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public String getPractica() {
		return practica;
	}

	public void setPractica(String practica) {
		this.practica = practica;
	}

	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getPerfilTecnico() {
		return perfilTecnico;
	}

	public void setPerfilTecnico(String perfilTecnico) {
		this.perfilTecnico = perfilTecnico;
	}

	public String getPrimarySkill() {
		return primarySkill;
	}

	public void setPrimarySkill(String primarySkill) {
		this.primarySkill = primarySkill;
	}

	public Date getFechaIncorporacion() {
		return fechaIncorporacion;
	}

	public void setFechaIncorporacion(Date fechaIncorporacion) {
		this.fechaIncorporacion = fechaIncorporacion;
	}

	public Integer getAsignacion() {
		return asignacion;
	}

	public void setAsignacion(Integer asignacion) {
		this.asignacion = asignacion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getClienteActual() {
		return clienteActual;
	}

	public void setClienteActual(String clienteActual) {
		this.clienteActual = clienteActual;
	}

	public Date getFechaInicioAsignacion() {
		return fechaInicioAsignacion;
	}

	public void setFechaInicioAsignacion(Date fechaInicioAsignacion) {
		this.fechaInicioAsignacion = fechaInicioAsignacion;
	}

	public Date getFechaFinAsignacion() {
		return fechaFinAsignacion;
	}

	public void setFechaFinAsignacion(Date fechaFinAsignacion) {
		this.fechaFinAsignacion = fechaFinAsignacion;
	}

	public Date getFechaDisponibilidad() {
		return fechaDisponibilidad;
	}

	public void setFechaDisponibilidad(Date fechaDisponibilidad) {
		this.fechaDisponibilidad = fechaDisponibilidad;
	}

	
	public String getPosicionProyectoFuturo() {
		return posionProyectoFuturo;
	}

	public void setPosicionProyectoFuturo(String posicionProyectoFuturo) {
		this.posionProyectoFuturo = posicionProyectoFuturo;
	}

	public String getColaboraciones() {
		return colaboraciones;
	}

	public void setColaboraciones(String colaboraciones) {
		this.colaboraciones = colaboraciones;
	}

	public String getProyectoAnterior() {
		return proyectoAnterior;
	}

	public void setProyectoAnterior(String proyectoAnterior) {
		this.proyectoAnterior = proyectoAnterior;
	}

	public String getInglesEscrito() {
		return inglesEscrito;
	}

	public void setInglesEscrito(String inglesEscrito) {
		this.inglesEscrito = inglesEscrito;
	}

	public String getInglesHablado() {
		return inglesHablado;
	}

	public void setInglesHablado(String inglesHablado) {
		this.inglesHablado = inglesHablado;
	}

	public String getJornada() {
		return jornada;
	}

	public void setJornada(String jornada) {
		this.jornada = jornada;
	}

	public String getMesesBench() {
		return mesesBench;
	}

	public void setMesesBench(String mesesBench) {
		this.mesesBench = mesesBench;
	}

	public String getPracticeArea() {
		return practiceArea;
	}

	public void setPracticeArea(String practiceArea) {
		this.practiceArea = practiceArea;
	}

	@Override
	public int compareTo(StaffingDataImport o) {
		return Integer.compare(this.getId(), o.getId());
	}

}
