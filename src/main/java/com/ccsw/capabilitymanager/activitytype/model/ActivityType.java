package com.ccsw.capabilitymanager.activitytype.model;

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

    @OneToOne(mappedBy = "tipoActividad")
    private Activity activity;

    public ActivityType() {
    }

    public ActivityType(String nombre) {
        this.nombre = nombre;
    }

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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
