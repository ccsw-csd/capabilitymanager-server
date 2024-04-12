package com.ccsw.dashboard.listadobench.model;

import java.util.Date;

public class ListadoBench {

    private Long id;
    private String saga;    
    private String ggid; 
    private String nombre; 
    private String apellidos;  
    private String practica;     
    private String grado;      
    private String categoria;  
    private String perfilTecnico;
    private Date fechaIncorporacion;  
    private Integer asignacion;     
    private String status;     
    private String clienteActual; 
    private Date fechaInicioAsignacion;  
    private Date fechaFinAsignacion;
    private Date fechaDisponibilidad;
    private String posicionProyectoFuturo;  
    private String colaboraciones;
    private String proyectoAnterior;
    private String mesesBench;
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return posicionProyectoFuturo;
	}

	public void setPosicionProyectoFuturo(String posicionProyectoFuturo) {
		this.posicionProyectoFuturo = posicionProyectoFuturo;
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

	public String getMesesBench() {
		return mesesBench;
	}

	public void setMesesBench(String mesesBench) {
		this.mesesBench = mesesBench;
	}
    
}
