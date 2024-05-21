package com.ccsw.capabilitymanager.activitytype.model;

public class ActivityTypeDTO {

    private Long id;
    private String nombre;

    // Constructor sin parámetros
    public ActivityTypeDTO() {
    }

    // Constructor con parámetros
    public ActivityTypeDTO(Long id, String nombre) {
        this.id = id;
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
}

