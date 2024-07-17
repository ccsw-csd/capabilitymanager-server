package com.ccsw.capabilitymanager.formdataimport.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "formdata")
@Table(name = "formdata")
public class FormDataImport implements Comparable<FormDataImport> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "num_import_code_id", nullable = false)
	private int numImportCodeId;

	@Column(name = "SAGA")
	private String SAGA;

	@Column(name = "email")
	private String email;

	@Column(name = "name")
	private String name;

	@Column(name = "rol_L1")
	private String rolL1;

	@Column(name = "rol_L1_extendido")
	private String rolL1Extendido;

	@Column(name = "rol_L2_EM")
	private String rolL2EM;

	@Column(name = "rol_L2_AR")
	private String RolL2AR;

	@Column(name = "rol_L2_AN")
	private String rolL2AN;

	@Column(name = "rol_L2_SE")
	private String rolL2SE;

	@Column(name = "rol_L3")
	private String rolL3;

	@Column(name = "rol_L4")
	private String rolL4;

	@Column(name = "rol_experience_EM")
	private String rolExperienceEM;

	@Column(name = "rol_experience_AR")
	private String rolExperienceAR;

	@Column(name = "skill_cloud_native_experience")
	private String skillCloudNativeExperience;

	@Column(name = "skill_low_code_experience")
	private String skillLowCodeExperience;

	@Column(name = "sector_experience")
	private String sectorExperience;

	@Column(name = "skill_cloud_experience")
	private String skillCloudExperience;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumImportCodeId() {
		return numImportCodeId;
	}

	public void setNumImportCodeId(int numImportCodeId) {
		this.numImportCodeId = numImportCodeId;
	}

	public String getSAGA() {
		return SAGA;
	}

	public void setSAGA(String saga) {
		this.SAGA = saga;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRolL1() {
		return rolL1;
	}

	public void setVcProfileRolL1(String rolL1) {
		this.rolL1 = rolL1;
	}

	public String getRolL1extendido() {
		return rolL1Extendido;
	}

	public void setRolL1Extendido(String rolL1Extendido) {
		this.rolL1Extendido = rolL1Extendido;
	}

	public String getRolL2EM() {
		return rolL2EM;
	}

	public void setRolL2EM(String rolL2EM) {
		this.rolL2EM = rolL2EM;
	}

	public String getRolL2AR() {
		return RolL2AR;
	}

	public void setRolL2AR(String rolL2AR) {
		this.RolL2AR = rolL2AR;
	}

	public String getRolL2AN() {
		return rolL2AN;
	}

	public void setRolL2AN(String rolL2AN) {
		this.rolL2AN = rolL2AN;
	}

	public String getRolL2SE() {
		return rolL2SE;
	}

	public void setRolL2SE(String rolL2SE) {
		this.rolL2SE = rolL2SE;
	}

	public String getRolL3() {
		return rolL3;
	}

	public void setRolL3(String rolL3) {
		this.rolL3 = rolL3;
	}

	public String getRolL4() {
		return rolL4;
	}

	public void setRolL4(String rolL4) {
		this.rolL4 = rolL4;
	}

	public String getRolExperienceEM() {
		return rolExperienceEM;
	}

	public void setRolExperienceEM(String rolExperienceEM) {
		this.rolExperienceEM = rolExperienceEM;
	}

	public String getRolExperienceAR() {
		return rolExperienceAR;
	}

	public void setRolExperienceAR(String rolExperienceAR) {
		this.rolExperienceAR = rolExperienceAR;
	}

	public String getSkillCloudNativeExperience() {
		return skillCloudNativeExperience;
	}

	public void setSkillCloudNativeExperience(String skillCloudNativeExperience) {
		this.skillCloudNativeExperience = skillCloudNativeExperience;
	}

	public String getSkillLowCodeExperience() {
		return skillLowCodeExperience;
	}

	public void setSkillLowCodeExperience(String skillLowCodeExperience) {
		this.skillLowCodeExperience = skillLowCodeExperience;
	}

	public String getSectorExperience() {
		return sectorExperience;
	}

	public void setSectorExperience(String sectorExperience) {
		this.sectorExperience = sectorExperience;
	}

	public String getSkillCloudExp() {
		return skillCloudExperience;
	}

	public void setSkillCloudExp(String skillCloudExp) {
		this.skillCloudExperience = skillCloudExp;
	}
	

	public String getRolL1Extendido() {
		return rolL1Extendido;
	}

	public void setRolL1(String rolL1) {
		this.rolL1 = rolL1;
	}

	@Override
	public int compareTo(FormDataImport o) {
		// TODO Auto-generated method stub
		return Integer.compare(this.getId(), o.getId());
	}

}
