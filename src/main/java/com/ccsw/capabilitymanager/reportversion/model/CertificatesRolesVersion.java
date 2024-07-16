package com.ccsw.capabilitymanager.reportversion.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "certificaciones_modelo_capacidad")
public class CertificatesRolesVersion  implements Comparable<CertificatesRolesVersion>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="SAGA", nullable = false)
    private String SAGA;
	
	@Column(name="Fecha_Carga", nullable = false)
    private Date fechaCarga;
	
	@Column(name="Usuario", nullable = false)
    private String usuario;
	
	@Column(name="Rol_Formulario", nullable = false)
    private String rolFormulario;
	
    @Column(name="Certificado", nullable = true)
    private String Certificado;

    @Column(name="num_import_code_id")
    private int num_import_code_id;
    
    
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getSAGA() {
		return SAGA;
	}



	public void setSAGA(String sAGA) {
		SAGA = sAGA;
	}



	public Date getFechaCarga() {
		return fechaCarga;
	}



	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
	}



	public String getUsuario() {
		return usuario;
	}



	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}



	public String getRolFormulario() {
		return rolFormulario;
	}



	public void setRolFormulario(String rolFormulario) {
		this.rolFormulario = rolFormulario;
	}



	public String getCertificado() {
		return Certificado;
	}



	public void setCertificado(String certificado) {
		Certificado = certificado;
	}
	
	public int getNum_import_code_id() {
		return num_import_code_id;
	}



	public void setNum_import_code_id(int num_import_code_id) {
		this.num_import_code_id = num_import_code_id;
	}



	@Override
	public int compareTo(CertificatesRolesVersion o) {
		return Long.valueOf(o.getId()).compareTo(this.id);
	}
}
