package com.ccsw.capabilitymanager.fileprocess.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_process")
public class FileProcess {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_fichero", nullable = false)
    private String tipoFichero;

    @Column(name = "nombre_fichero", nullable = false)
    private String nombreFichero;

    @Column(name = "nombre_bucket", nullable = false)
    private String nombreBucket;

    @Column(name = "fecha_importacion", nullable = false)
    private LocalDateTime fechaImportacion;

    @Column(name = "usuario", nullable = true)
    private String usuario;

    @Column(name = "estado", nullable = false)
    private String estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoFichero() {
        return tipoFichero;
    }

    public void setTipoFichero(String tipoFichero) {
        this.tipoFichero = tipoFichero;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    public String getNombreBucket() {
        return nombreBucket;
    }

    public void setNombreBucket(String nombreBucket) {
        this.nombreBucket = nombreBucket;
    }

    public LocalDateTime getFechaImportacion() {
        return fechaImportacion;
    }

    public void setFechaImportacion(LocalDateTime fechaImportacion) {
        this.fechaImportacion = fechaImportacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
