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
    private String Ggid; 
    
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
    private String perfil_Tecnico;
    
    @Column(name="vc_Profile_Fecha_Incorporacion")
    private Date fecha_Incorporacion;  
    
    @Column(name="vc_Profile_Asignacion")
    private Integer asignacion;     
    
    @Column(name="vc_Profile_Status")
    private String status;     
    
    @Column(name="vc_Profile_Cliente_Actual")
    private String cliente_ctual; 
    
    @Column(name="vc_Profile_Fecha_Inicio_Asignacion")
    private Date fecha_Inicio_asignacion;  
    
    @Column(name="vc_Profile_Fecha_Fin_Asignacion")
    private Date fecha_Fin_signacion;
    
    @Column(name="vc_Profile_Fecha_Disponibilidad")
    private Date fecha_Disponibilidad;
    
    @Column(name="vc_Profile_Posicion_Proyecto_Futuro")
    private String posicion_Proyecto_Futuro;  
    
    @Column(name="vc_Profile_Colaboraciones")
    private String colaboraciones;
    
    @Column(name="vc_Profile_Proyecto_Anterior")
    private String proyecto_anterior;
    
    @Column(name="vc_Profile_Meses_Bench")
    private String Meses_Bench;  
    

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
		return Ggid;
	}

	public void setGgid(String ggid) {
		Ggid = ggid;
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

	public String getPerfil_Tecnico() {
		return perfil_Tecnico;
	}

	public void setPerfil_Tecnico(String perfil_Tecnico) {
		this.perfil_Tecnico = perfil_Tecnico;
	}

	public Date getFecha_Incorporacion() {
		return fecha_Incorporacion;
	}

	public void setFecha_Incorporacion(Date fecha_Incorporacion) {
		this.fecha_Incorporacion = fecha_Incorporacion;
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

	public String getCliente_ctual() {
		return cliente_ctual;
	}

	public void setCliente_ctual(String cliente_ctual) {
		this.cliente_ctual = cliente_ctual;
	}

	public Date getFecha_Inicio_asignacion() {
		return fecha_Inicio_asignacion;
	}

	public void setFecha_Inicio_asignacion(Date fecha_Inicio_asignacion) {
		this.fecha_Inicio_asignacion = fecha_Inicio_asignacion;
	}

	public Date getFecha_Fin_signacion() {
		return fecha_Fin_signacion;
	}

	public void setFecha_Fin_signacion(Date fecha_Fin_signacion) {
		this.fecha_Fin_signacion = fecha_Fin_signacion;
	}

	public Date getFecha_Disponibilidad() {
		return fecha_Disponibilidad;
	}

	public void setFecha_Disponibilidad(Date fecha_Disponibilidad) {
		this.fecha_Disponibilidad = fecha_Disponibilidad;
	}

	public String getPosicion_Proyecto_Futuro() {
		return posicion_Proyecto_Futuro;
	}

	public void setPosicion_Proyecto_Futuro(String posicion_Proyecto_Futuro) {
		this.posicion_Proyecto_Futuro = posicion_Proyecto_Futuro;
	}

	public String getColaboraciones() {
		return colaboraciones;
	}

	public void setColaboraciones(String colaboraciones) {
		this.colaboraciones = colaboraciones;
	}

	public String getProyecto_anterior() {
		return proyecto_anterior;
	}

	public void setProyecto_anterior(String proyecto_anterior) {
		this.proyecto_anterior = proyecto_anterior;
	}

	public String getMeses_Bench() {
		return Meses_Bench;
	}

	public void setMeses_Bench(String meses_Bench) {
		Meses_Bench = meses_Bench;
	}
    
}
