package com.ccsw.capabilitymanager.certificatesdataimport.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "certificaciones")
public class CertificatesDataImport  implements Comparable<CertificatesDataImport>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "saga", nullable = false)
	private String SAGA;

	@Column(name = "partner", nullable = false)
	private String partner;

	@Column(name = "certificado")
	private String certificado;

	@Column(name = "name_gtd")
	private String nameGTD;

	@Column(name = "certification_gtd")
	private String certificationGTD;
	
	@Column(name = "code")
	private String code;

	@Column(name = "sector")
	private String sector;

	@Column(name = "modulo")
	private String modulo;
	
	@Column(name = "id_candidato")
	private String idCandidato;

	@Column(name = "fecha_certificado", nullable = false)
	private Date fechaCertificado;
	
	@Column(name = "fecha_expiracion")
	private Date fechaExpiracion;

	@Column(name = "activo")
	private String activo;

	@Column(name = "anexo")
	private String anexo;

	@Column(name = "comentario_anexo")
	private String comentarioAnexo;
	
	@Column(name = "num_import_code_id")
    private int numImportCodeId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSAGA() {
		return SAGA;
	}

	public void setSAGA(String saga) {
		this.SAGA = saga;
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

	public String getNameGTD() {
		return nameGTD;
	}

	public void setNameGTD(String nameGTD) {
		this.nameGTD = nameGTD;
	}

	public String getCertificationGTD() {
		return certificationGTD;
	}

	public void setCertificationGTD(String certificationGTD) {
		this.certificationGTD = certificationGTD;
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

	public void setSecto(String sector) {
		this.sector = sector;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getIdCandidato() {
		return idCandidato;
	}

	public void setIdCandidato(String idCandidato) {
		this.idCandidato = idCandidato;
	}

	public Date getFechaCertificado() {
		return fechaCertificado;
	}

	public void setFechaCertificado(Date fechaCertificado) {
		this.fechaCertificado = fechaCertificado;
	}

	public Date getVcFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(Date fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
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

	public String getComentarioAnexo() {
		return comentarioAnexo;
	}

	public void setComentarioAnexo(String comentarioAnexo) {
		this.comentarioAnexo = comentarioAnexo;
	}

	public int getNumImportCodeId() {
		return numImportCodeId;
	}

	public void setNumImportCodeId(int num_import_code_id) {
		this.numImportCodeId = num_import_code_id;
	}

	@Override
	public int compareTo(CertificatesDataImport o) {
		
		return 0;
	}

	
}
