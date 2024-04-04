package com.ccsw.dashboard.dataformation.model;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name="vista_listado_bench")
public class DataFormation {

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
    
    @Column(name="vc_partner")
    private String partner;
    
    @Column(name="vc_certificado")
    private String certificado;
    
    @Column(name="vc_name_gtd")
    private String name_gtd;
    
    @Column(name="vc_certification_gtd")
    private String certification_gtd;  
    
    @Column(name="vc_code")
    private String code;
    
    @Column(name="vc_sector")
    private String sector;
    
    @Column(name="vc_modulo")
    private String modulo; 
    
    @Column(name="vc_id_candidato")
    private String id_candidato;
    
    @Column(name="vc_fecha_certificado")
    private Date fecha_certificado;  
    
    @Column(name="vc_fecha_expiracion")
    private Date fecha_expiracion;  
    
    @Column(name="vc_activo")
    private String activo; 
    
    @Column(name="vc_anexo")
    private String anexo;  
    
    @Column(name="vc_Profile_Rol_L1")
    private String comentario_anexo; 
    
    @Column(name="vc_comentario_anexo")
    private String Profile_Rol_L1;

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

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getCertificado() {
		return certificado;
	}

	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}

	public String getName_gtd() {
		return name_gtd;
	}

	public void setName_gtd(String name_gtd) {
		this.name_gtd = name_gtd;
	}

	public String getCertification_gtd() {
		return certification_gtd;
	}

	public void setCertification_gtd(String certification_gtd) {
		this.certification_gtd = certification_gtd;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getId_candidato() {
		return id_candidato;
	}

	public void setId_candidato(String id_candidato) {
		this.id_candidato = id_candidato;
	}

	public Date getFecha_certificado() {
		return fecha_certificado;
	}

	public void setFecha_certificado(Date fecha_certificado) {
		this.fecha_certificado = fecha_certificado;
	}

	public Date getFecha_expiracion() {
		return fecha_expiracion;
	}

	public void setFecha_expiracion(Date fecha_expiracion) {
		this.fecha_expiracion = fecha_expiracion;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getAnexo() {
		return anexo;
	}

	public void setAnexo(String anexo) {
		this.anexo = anexo;
	}

	public String getComentario_anexo() {
		return comentario_anexo;
	}

	public void setComentario_anexo(String comentario_anexo) {
		this.comentario_anexo = comentario_anexo;
	}

	public String getProfile_Rol_L1() {
		return Profile_Rol_L1;
	}

	public void setProfile_Rol_L1(String profile_Rol_L1) {
		Profile_Rol_L1 = profile_Rol_L1;
	}
    
}


