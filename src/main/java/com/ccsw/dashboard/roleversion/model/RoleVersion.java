package com.ccsw.dashboard.roleversion.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "version_capacidades")
public class RoleVersion  implements Comparable<RoleVersion>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="id_Tipo_interfaz", nullable = false)
    private int idTipoInterfaz;	

    @Column(name="importacion", nullable = false)
    private LocalDateTime fechaImportacion;
    
    @Column(name="num_Registros", nullable = false)
    private int numRegistros;
    
    @Column(name="nombre_Fichero", nullable = false)
    private String nombreFichero;
    
    @Column(name="descripcion", nullable = false)
    private String descripcion;
    
    @Column(name="usuario", nullable = false)
    private String usuario;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getIdTipoInterfaz() {
		return idTipoInterfaz;
	}

	public void setIdTipoInterfaz(int idTipoInterfaz) {
		this.idTipoInterfaz = idTipoInterfaz;
	}

	@Override	
	public int compareTo(RoleVersion o) {
		return Integer.valueOf(o.getId()).compareTo(this.id);
	}	
}
