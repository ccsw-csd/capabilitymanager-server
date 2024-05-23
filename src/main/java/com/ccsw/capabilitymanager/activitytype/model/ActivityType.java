package com.ccsw.capabilitymanager.activitytype.model;

import java.util.List;

import com.ccsw.capabilitymanager.activity.model.Activity;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_actividades")
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_actividad")
    private String nombre;
    
    @OneToMany(mappedBy = "tipoActividad")
    private List<Activity> activities;

    // Constructor sin parámetros
    public ActivityType() {
    }

    // Constructor con parámetros
    public ActivityType(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
}
