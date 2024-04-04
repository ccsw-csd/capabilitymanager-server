package com.ccsw.dashboard.staffingdataimport.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dm_staffing_import")
public class StaffingDataImport implements Comparable<StaffingDataImport> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "num_import_codeid", nullable = false)
	private int numImportCode;

	@Column(name = "vc_Profile_SAGA", nullable = false)
	private String vcProfileSAGA;

	@Column(name = "vc_Profile_GGID", nullable = false)
	private String vcProfileGGID;

	@Column(name = "vc_Profile_Centro", nullable = false)
	private String vcProfileCentro;

	@Column(name = "vc_Profile_Nombre", nullable = false)
	private String vcProfileNombre;

	@Column(name = "vc_Profile_Apellidos")
	private String vcProfileApellidos;

	@Column(name = "vc_Profile_Localizacion")
	private String vcProfileLocalizacion;

	@Column(name = "vc_Profile_Practica", nullable = false)
	private String vcProfilePractica;

	@Column(name = "vc_Profile_Grado", nullable = false)
	private String vcProfileGrado;

	@Column(name = "vc_Profile_Categoria")
	private String vcProfileCategoria;

	@Column(name = "vc_Profile_Perfil_Tecnico")
	private String vcProfilePerfilTecnico;

	@Column(name = "vc_Profile_Fecha_Incorporacion")
	private String vcProfileFechaIncorporacion;
	
	@Column(name = "vc_Profile_Asignacion")
	private String vcProfileAsignacion;
	
	@Column(name = "vc_Profile_Status")
	private String vcProfileStatus;

	@Column(name = "vc_Profile_Cliente_Actual")
	private String vcProfileClienteActual;
	
	@Column(name = "vc_Profile_Fecha_Inicio_Asignacion")
	private String vcProfileFechaInicioAsignacion;
	
	@Column(name = "vc_Profile_Fecha_Fin_Asignacion")
	private String vcProfileFechaFinAsignacion;

	@Column(name = "vc_Profile_Disponibilidad")
	private String vcProfileDisponibilidad;

	@Column(name = "vc_Profile_Posicion_Proyecto_Futuro")
	private String vcProfilePosionProyectoFuturo;
	
	@Column(name = "vc_Profile_Colaboraciones")
	private String vcProfileColaboraciones;
	
	@Column(name = "vc_Profile_Proyecto_Anterior")
	private String vcProfileProyectoAnterior;
	
	@Column(name = "vc_Profile_Meses_Bench")
	private String vcProfileMesesBench;

	
	
	public int getNumImportCode() {
		return numImportCode;
	}

	public void setNumImportCode(int numImportCode) {
		this.numImportCode = numImportCode;
	}

	public String getVcProfileFechaIncorporacion() {
		return vcProfileFechaIncorporacion;
	}

	public void setVcProfileFechaIncorporacion(String vcProfileFechaIncorporacion) {
		this.vcProfileFechaIncorporacion = vcProfileFechaIncorporacion;
	}


	public String getVcProfileAsignacion() {
		return vcProfileAsignacion;
	}

	public void setVcProfileAsignacion(String vcProfileAsignacion) {
		this.vcProfileAsignacion = vcProfileAsignacion;
	}

	public String getVcProfileClienteActual() {
		return vcProfileClienteActual;
	}

	public void setVcProfileClienteActual(String vcProfileClienteActual) {
		this.vcProfileClienteActual = vcProfileClienteActual;
	}

	public String getVcProfileFechaInicioAsignacion() {
		return vcProfileFechaInicioAsignacion;
	}

	public void setVcProfileFechaInicioAsignacion(String vcProfileFechaInicioAsignacion) {
		this.vcProfileFechaInicioAsignacion = vcProfileFechaInicioAsignacion;
	}

	public String getVcProfileFechaFinAsignacion() {
		return vcProfileFechaFinAsignacion;
	}

	public void setVcProfileFechaFinAsignacion(String vcProfileFechaFinAsignacion) {
		this.vcProfileFechaFinAsignacion = vcProfileFechaFinAsignacion;
	}

	public String getVcProfileDisponibilidad() {
		return vcProfileDisponibilidad;
	}

	public void setVcProfileDisponibilidad(String vcProfileDisponibilidad) {
		this.vcProfileDisponibilidad = vcProfileDisponibilidad;
	}

	public String getVcProfileProyectoFuturo() {
		return vcProfilePosionProyectoFuturo;
	}

	public void setVcProfileProyectoFuturo(String vcProfileProyectoFuturo) {
		this.vcProfilePosionProyectoFuturo = vcProfileProyectoFuturo;
	}

	public String getVcProfileColaboraciones() {
		return vcProfileColaboraciones;
	}

	public void setVcProfileColaboraciones(String vcProfileColaboraciones) {
		this.vcProfileColaboraciones = vcProfileColaboraciones;
	}

	public String getVcProfileProyectoAnterior() {
		return vcProfileProyectoAnterior;
	}

	public void setVcProfileProyectoAnterior(String vcProfileProyectoAnterior) {
		this.vcProfileProyectoAnterior = vcProfileProyectoAnterior;
	}

	public String getVcProfileMesesBench() {
		return vcProfileMesesBench;
	}

	public void setVcProfileMesesBench(String vcProfileMesesBench) {
		this.vcProfileMesesBench = vcProfileMesesBench;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumImportCodeID() {
		return numImportCode;
	}

	public void setNumImportCodeID(int numImportCodeID) {
		this.numImportCode = numImportCodeID;
	}

	public String getVcProfileSAGA() {
		return vcProfileSAGA;
	}

	public void setVcProfileSAGA(String vcProfileSAGA) {
		this.vcProfileSAGA = vcProfileSAGA;
	}

	public String getVcProfileGGID() {
		return vcProfileGGID;
	}

	public void setVcProfileGGID(String vcProfileGGID) {
		this.vcProfileGGID = vcProfileGGID;
	}

	public String getVcProfilePractica() {
		return vcProfilePractica;
	}

	public void setVcProfilePractica(String vcProfilePractica) {
		this.vcProfilePractica = vcProfilePractica;
	}

	public String getVcProfileGrado() {
		return vcProfileGrado;
	}

	public void setVcProfileGrado(String vcProfileGrado) {
		this.vcProfileGrado = vcProfileGrado;
	}

	public String getVcProfileCategoria() {
		return vcProfileCategoria;
	}

	public void setVcProfileCategoria(String vcProfileCategoria) {
		this.vcProfileCategoria = vcProfileCategoria;
	}

	public String getVcProfileCentro() {
		return vcProfileCentro;
	}

	public void setVcProfileCentro(String vcProfileCentro) {
		this.vcProfileCentro = vcProfileCentro;
	}

	public String getVcProfileNombre() {
		return vcProfileNombre;
	}

	public void setVcProfileNombre(String vcProfileNombre) {
		this.vcProfileNombre = vcProfileNombre;
	}

	public String getVcProfileApellidos() {
		return vcProfileApellidos;
	}

	public void setVcProfileApellidos(String vcProfileApellidos) {
		this.vcProfileApellidos = vcProfileApellidos;
	}

	public String getVcProfileLocalizacion() {
		return vcProfileLocalizacion;
	}

	public void setVcProfileLocalizacion(String vcProfileLocalizacion) {
		this.vcProfileLocalizacion = vcProfileLocalizacion;
	}

	public String getVcProfilePerfilTecnico() {
		return vcProfilePerfilTecnico;
	}

	public void setVcProfilePerfilTecnico(String vcProfilePerfilTecnico) {
		this.vcProfilePerfilTecnico = vcProfilePerfilTecnico;
	}

	public String getVcProfileStatus() {
		return vcProfileStatus;
	}

	public void setVcProfileStatus(String vcProfileStatus) {
		this.vcProfileStatus = vcProfileStatus;
	}

	@Override
	public int compareTo(StaffingDataImport o) {
		// TODO Auto-generated method stub
		return Integer.compare(this.getId(), o.getId());
	}

}
