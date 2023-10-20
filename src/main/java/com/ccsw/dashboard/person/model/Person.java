package com.ccsw.dashboard.person.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dm_synthesisdata_import")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vc_Profile_Name", nullable = false)
    private String name;

    @Column(name="vc_Skill_Type", nullable = false)
    private String skillType;

    @Column(name="num_Skill_Level", nullable = false)
    private int skyllLevel;

    @Column(name="vc_Skill_Description", nullable = false)
    private String skillDescription;

    //@Column(name = "vc_Profile_RegNumber")
    // private String profileRegNumber;

    @Column(name = "vc_Profile_TalentID", nullable = false)
    private String tanlentID;

    @Column(name = "vc_Profile_Location", nullable = false)
    private String location;

    @Column(name = "vc_Profile_Circle", nullable = false)
    private String profileCircle;

    @Column(name = "vc_Profile_Type", nullable = false)
    private String profileType;

    @Column(name = "vc_Profile_Company")
    private String company;

    @Column(name = "vc_Profile_Manager")
    private String manager;

    @Column(name = "vc_Profile_Mentor")
    private String mentor;

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
