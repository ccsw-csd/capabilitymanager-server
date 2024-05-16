package com.ccsw.capabilitymanager.skill.model;

import jakarta.persistence.*;

@Entity
@Table(name = "synthesisdata_import")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="vc_skill_type", nullable = false)
    private String skillType;

    @Column(name="num_skill_Level", nullable = false)
    private int skyllLevel;

    @Column(name="skill_description", nullable = false)
    private String skillDescription;

    @Column(name = "talentID", nullable = false)
    private String tanlentID;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "circle", nullable = false)
    private String profileCircle;

    @Column(name = "type", nullable = false)
    private String profileType;

    @Column(name = "company")
    private String company;

    @Column(name = "manager")
    private String manager;

    @Column(name = "mentor")
    private String mentor;

    public Skill() {
    }

    public Skill(String name) {
        this.name = name;
    }

    public Skill(Long id, String name) {
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

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public int getSkyllLevel() {
        return skyllLevel;
    }

    public void setSkyllLevel(int skyllLevel) {
        this.skyllLevel = skyllLevel;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public String getTanlentID() {
        return tanlentID;
    }

    public void setTanlentID(String tanlentID) {
        this.tanlentID = tanlentID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileCircle() {
        return profileCircle;
    }

    public void setProfileCircle(String profileCircle) {
        this.profileCircle = profileCircle;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }
}
