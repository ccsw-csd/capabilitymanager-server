package com.ccsw.capabilitymanager.staffingdataimport.model;

import java.util.Date;

import jakarta.persistence.Column;

@SuppressWarnings("unused")
public class StaffingDataImportDto {

	private int id;
	private int numImportCodeId;
	private String vcProfileSAGA;
	private String vcProfileGGID;
	private String vcProfileNombre;
	private String vcProfileApellidos;
	private String vcProfilePractica;
	private String vcProfileGrado;
	private String vcProfileCategoria;
	private String vcProfilePerfilTecnico;	
	private String vcProfileCentro;
	private String vcProfileLocalizacion;
	private Date vcFechaIncorporacion;
	private String vcAsignacion;
	private String vcProfileStatus;
	private String vcClienteActual;
	private Date vcFechaInicioAsignacion;
	private Date vcFechaFinAsignacion;
	private Date vcFechaDisponibilidad;
	private String vcPosicionProyectoFuturo;
	private String vcColaboraciones;
	private String vcProyectoAnterior;
	private String vcMesesBench;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumImportCodeId() {
		return numImportCodeId;
	}

	public void setNumImportCodeId(int numImportCodeId) {
		this.numImportCodeId = numImportCodeId;
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

	public Date getVcFechaIncorporacion() {
		return vcFechaIncorporacion;
	}

	public void setVcFechaIncorporacion(Date vcFechaIncorporacion) {
		this.vcFechaIncorporacion = vcFechaIncorporacion;
	}

	public String getVcAsignacion() {
		return vcAsignacion;
	}

	public void setVcAsignacion(String vcAsignacion) {
		this.vcAsignacion = vcAsignacion;
	}

	public String getVcClienteActual() {
		return vcClienteActual;
	}

	public void setVcClienteActual(String vcClienteActual) {
		this.vcClienteActual = vcClienteActual;
	}

	public Date getVcFechaInicioAsignacion() {
		return vcFechaInicioAsignacion;
	}

	public void setVcFechaInicioAsignacion(Date vcFechaInicioAsignacion) {
		this.vcFechaInicioAsignacion = vcFechaInicioAsignacion;
	}

	public Date getVcFechaFinAsignacion() {
		return vcFechaFinAsignacion;
	}

	public void setVcFechaFinAsignacion(Date vcFechaFinAsignacion) {
		this.vcFechaFinAsignacion = vcFechaFinAsignacion;
	}

	public Date getVcFechaDisponibilidad() {
		return vcFechaDisponibilidad;
	}

	public void setVcFechaDisponibilidad(Date vcFechaDisponibilidad) {
		this.vcFechaDisponibilidad = vcFechaDisponibilidad;
	}

	public String getVcPosicionProyectoFuturo() {
		return vcPosicionProyectoFuturo;
	}

	public void setVcPosicionProyectoFuturo(String vcPosicionProyectoFuturo) {
		this.vcPosicionProyectoFuturo = vcPosicionProyectoFuturo;
	}

	public String getVcColaboraciones() {
		return vcColaboraciones;
	}

	public void setVcColaboraciones(String vcColaboraciones) {
		this.vcColaboraciones = vcColaboraciones;
	}

	public String getVcProyectoAnterior() {
		return vcProyectoAnterior;
	}

	public void setVcProyectoAnterior(String vcProyectoAnterior) {
		this.vcProyectoAnterior = vcProyectoAnterior;
	}

	public String getVcMesesBench() {
		return vcMesesBench;
	}

	public void setVcMesesBench(String vcMesesBench) {
		this.vcMesesBench = vcMesesBench;
	}

}
