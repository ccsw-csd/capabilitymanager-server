package com.ccsw.dashboard.config.person.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dm_synthesisdata_import")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vc_Profile_Name", nullable = false)
    private String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
