package com.ccsw.dashboard.versioncertificados.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.ccsw.dashboard.certificatesdataimport.model.CertificatesDataImport;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "version_certificados")
public class VersionCertificaciones {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "id_tipo_interfaz", nullable = false)
	private int idTipointerfaz;
	
	@Column(name = "fecha_importacion", nullable = false)
	private LocalDateTime fechaImportacion;
	
	@Column(name = "num_registros", nullable = false)
	private int numRegistros;

	@Column(name = "nombre_fichero", nullable = false)
	private String nombreFichero;

	@Column(name = "description")
	private String description;

	@Column(name = "usuario")
	private String usuario;
	
	@Lob
	@Column(name = "fichero")
	private byte[] fichero;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="id")
	private Set<CertificatesDataImport> certificates;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdTipointerfaz() {
		return idTipointerfaz;
	}

	public void setIdTipointerfaz(int idTipointerfaz) {
		this.idTipointerfaz = idTipointerfaz;
	}

	public LocalDateTime getFechaImportacion() {
		return fechaImportacion;
	}

	public void setFechaImportacion(LocalDateTime fechaImportacion) {
		this.fechaImportacion = fechaImportacion;
	}

	public int getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	public String getNombreFichero() {
		return nombreFichero;
	}

	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public byte[] getFichero() {
		return fichero;
	}

	public void setFichero(byte[] fichero) {
		this.fichero = fichero;
	}

	public Set<CertificatesDataImport> getCertificates() {
		return certificates;
	}

	public void setCertificates(Set<CertificatesDataImport> certificates) {
		this.certificates = certificates;
	}
	
}