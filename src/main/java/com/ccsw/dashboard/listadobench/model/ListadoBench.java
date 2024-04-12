package com.ccsw.dashboard.listadobench.model;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name="vista_listado_bench")
public class ListadoBench {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="vc_Profile_SAGA")
    private String saga;    
    
    @Column(name="vc_Profile_GGID ggid")
    private String ggid; 
    
    @Column(name="vc_Profile_Nombre")
    private String nombre; 
    
    @Column(name="vc_Profile_Apellidos")
    private String apellidos;  
    
    @Column(name="vc_Profile_Practica")
    private String practica;     
    
    @Column(name="vc_Profile_Grado")
    private String grado;      
    
    @Column(name="vc_Profile_Categoria")
    private String categoria;  
    
    @Column(name="vc_Profile_Perfil_Tecnico")
    private String perfilTecnico;
    
    @Column(name="vc_Profile_Fecha_Incorporacion")
    private Date fechaIncorporacion;  
    
    @Column(name="vc_Profile_Asignacion")
    private Integer asignacion;     
    
    @Column(name="vc_Profile_Status")
    private String status;     
    
    @Column(name="vc_Profile_Cliente_Actual")
    private String clienteActual; 
    
    @Column(name="vc_Profile_Fecha_Inicio_Asignacion")
    private Date fechaInicioAsignacion;  
    
    @Column(name="vc_Profile_Fecha_Fin_Asignacion")
    private Date fechaFinAsignacion;
    
    @Column(name="vc_Profile_Fecha_Disponibilidad")
    private Date fechaDisponibilidad;
    
    @Column(name="vc_Profile_Posicion_Proyecto_Futuro")
    private String posicionProyectoFuturo;  
    
    @Column(name="vc_Profile_Colaboraciones")
    private String colaboraciones;
    
    @Column(name="vc_Profile_Proyecto_Anterior")
    private String proyectoAnterior;
    
    @Column(name="vc_Profile_Meses_Bench")
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
