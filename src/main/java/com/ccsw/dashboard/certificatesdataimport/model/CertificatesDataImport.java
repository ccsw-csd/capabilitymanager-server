package com.ccsw.dashboard.certificatesdataimport.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "dm_certificaciones_import")
@Table(name = "dm_certificaciones_import")
public class CertificatesDataImport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "vc_saga", nullable = false)
	private String vcSAGA;

	@Column(name = "vc_partner", nullable = false)
	private String vcPartner;

	@Column(name = "vc_certificado")
	private String vcCertificado;

	@Column(name = "vc_name_gtd")
	private String vcNameGTD;

	@Column(name = "vc_certification_gtd")
	private String vcCertificationGTD;
	
	@Column(name = "vc_code")
	private String vcCode;

	@Column(name = "vc_sector")
	private String vcSector;

	@Column(name = "vc_modulo")
	private String vcModulo;
	
	@Column(name = "vc_id_candidato")
	private String vcIdCandidato;

	@Column(name = "vc_fecha_certificado")
	private Date vcFechaCertificado;
	
	@Column(name = "vc_fecha_expiracion")
	private Date vcFechaExpiracion;

	@Column(name = "vc_activo")
	private String vcActivo;

	@Column(name = "vc_anexo")
	private String vcAnexo;

	@Column(name = "vc_comentario_anexo")
	private String vcComentarioAnexo;
	
	@Column(name = "num_import_code_id")
	private int numImportCode;
	
//	@ManyToOne
//    @JoinColumn(name = "num_import_code_id")
//    private VersionCertificaciones num_import_code_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVcSAGA() {
		return vcSAGA;
	}

	public void setVcSAGA(String vcSAGA) {
		this.vcSAGA = vcSAGA;
	}

	public String getVcPartner() {
		return vcPartner;
	}

	public void setVcPartner(String vcPartner) {
		this.vcPartner = vcPartner;
	}

	public String getVcCertificado() {
		return vcCertificado;
	}

	public void setVcCertificado(String vcCertificado) {
		this.vcCertificado = vcCertificado;
	}

	public String getVcNameGTD() {
		return vcNameGTD;
	}

	public void setVcNameGTD(String vcNameGTD) {
		this.vcNameGTD = vcNameGTD;
	}

	public String getVcCertificationGTD() {
		return vcCertificationGTD;
	}

	public void setVcCertificationGTD(String vcCertificationGTD) {
		this.vcCertificationGTD = vcCertificationGTD;
	}

	public String getVcCode() {
		return vcCode;
	}

	public void setVcCode(String vcCode) {
		this.vcCode = vcCode;
	}

	public String getVcSector() {
		return vcSector;
	}

	public void setVcSector(String vcSector) {
		this.vcSector = vcSector;
	}

	public String getVcModulo() {
		return vcModulo;
	}

	public void setVcModulo(String vcModulo) {
		this.vcModulo = vcModulo;
	}

	public String getVcIdCandidato() {
		return vcIdCandidato;
	}

	public void setVcIdCandidato(String vcIdCandidato) {
		this.vcIdCandidato = vcIdCandidato;
	}

	public Date getVcFechaCertificado() {
		return vcFechaCertificado;
	}

	public void setVcFechaCertificado(Date vcFechaCertificado) {
		this.vcFechaCertificado = vcFechaCertificado;
	}

	public Date getVcFechaExpiracion() {
		return vcFechaExpiracion;
	}

	public void setVcFechaExpiracion(Date vcFechaExpiracion) {
		this.vcFechaExpiracion = vcFechaExpiracion;
	}

	public String getVcActivo() {
		return vcActivo;
	}

	public void setVcActivo(String vcActivo) {
		this.vcActivo = vcActivo;
	}

	public String getVcAnexo() {
		return vcAnexo;
	}

	public void setVcAnexo(String vcAnexo) {
		this.vcAnexo = vcAnexo;
	}

	public String getVcComentarioAnexo() {
		return vcComentarioAnexo;
	}

	public void setVcComentarioAnexo(String vcComentarioAnexo) {
		this.vcComentarioAnexo = vcComentarioAnexo;
	}

	public int getNumImportCode() {
		return numImportCode;
	}

	public void setNumImportCode(int num_import_code_id) {
		this.numImportCode = num_import_code_id;
	}
	
	
/*
	public VersionCertificaciones getNum_import_code_id() {
		return num_import_code_id;
	}

	public void setNum_import_code_id(VersionCertificaciones num_import_code_id) {
		this.num_import_code_id = num_import_code_id;
	}
*/
	
}
