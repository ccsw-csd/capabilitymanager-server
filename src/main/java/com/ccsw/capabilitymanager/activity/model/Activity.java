package com.ccsw.capabilitymanager.activity.model;

import com.ccsw.capabilitymanager.activitytype.model.ActivityType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "actividad")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_actividad_id",insertable = false, updatable = false)
    private ActivityType tipoActividad;

    @Column(name = "codigo_actividad", nullable = false)
    private String codigoActividad;

    @Column(name = "nombre_actividad", nullable = false)
    private String nombreActividad;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_ultima_actividad")
    private Date fechaUltimaActividad;

    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_finalizacion")
    private Date fechaFinalizacion;

    @Column(name = "porcentaje_avance", precision = 5, scale = 2)
    private BigDecimal porcentajeAvance;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "saga")
    private String saga;

    @Column(name = "ggid")
    private String ggid;

    @Column(name = "tipo_actividad_id")
    private Long tipoActividadId;

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActivityType getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(ActivityType tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(String codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaUltimaActividad() {
        return fechaUltimaActividad;
    }

    public void setFechaUltimaActividad(Date fechaUltimaActividad) {
        this.fechaUltimaActividad = fechaUltimaActividad;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public BigDecimal getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(BigDecimal porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getSaga() {
        return saga;
    }

    public void setSaga(String saga) {
        this.saga = saga;
    }

    public String getGgid() {
        return ggid;
    }

    public void setGgid(String ggid) {
        this.ggid = ggid;
    }

    public Long getTipoActividadId() {
        return tipoActividadId;
    }

    public void setTipoActividadId(Long tipoActividadId) {
        this.tipoActividadId = tipoActividadId;
    }
}
