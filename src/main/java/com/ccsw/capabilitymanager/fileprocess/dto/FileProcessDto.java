package com.ccsw.capabilitymanager.fileprocess.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class FileProcessDto {

    private Long id;

    private String tipoFichero;

    private String nombreFichero;

    private String nombreBucket;

    private LocalDateTime fechaImportacion;

    private String usuario;

    private String estado;

    private MultipartFile fileData;

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

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }
}
