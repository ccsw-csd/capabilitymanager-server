package com.ccsw.dashboard.certificacionesdataimport.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dm_formdata_import")
public class FormDataImport implements Comparable<FormDataImport> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "num_import_code_id", nullable = false)
	private int numImportCodeId;

	@Column(name = "vc_Profile_SAGA")
	private String vcProfileSAGA;

	@Column(name = "vc_Profile_Email")
	private String vcProfileEmail;

	@Column(name = "vc_Profile_Name")
	private String vcProfileName;

	@Column(name = "vc_Profile_Rol_L1")
	private String vcProfileRolL1;

	@Column(name = "vc_Profile_Rol_L1_extendido")
	private String vcProfileRolL1extendido;

	@Column(name = "vc_Profile_Rol_L2_EM")
	private String vcProfileRolL2EM;

	@Column(name = "vc_Profile_Rol_L2_AR")
	private String vcProfileRolL2AR;

	@Column(name = "vc_Profile_Rol_L2_AN")
	private String vcProfileRolL2AN;

	@Column(name = "vc_Profile_Rol_L2_SE")
	private String vcProfileRolL2SE;

	@Column(name = "vc_Profile_Rol_L3")
	private String vcProfileRolL3;

	@Column(name = "vc_Profile_Rol_L4")
	private String vcProfileRolL4;

	@Column(name = "vc_Profile_Rol_Experience_EM")
	private String vcProfileRolExperienceEM;

	@Column(name = "vc_Profile_Rol_Experience_AR")
	private String vcProfileRolExperienceAR;

	@Column(name = "vc_Profile_Skill_Cloud_Native_Experience")
	private String vcProfileSkillCloudNativeExperience;

	@Column(name = "vc_Profile_Skill_Low_Code_Experience")
	private String vcProfileSkillLowCodeExperience;

	@Column(name = "vc_Profile_Sector_Experience")
	private String vcProfileSectorExperience;

	@Column(name = "vc_Profile_Skill_Cloud_Exp")
	private String vcProfileSkillCloudExp;

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

	public String getVcProfileSAGA() {
		return vcProfileSAGA;
	}

	public void setVcProfileSAGA(String vcProfileSAGA) {
		this.vcProfileSAGA = vcProfileSAGA;
	}

	public String getVcProfileEmail() {
		return vcProfileEmail;
	}

	public void setVcProfileEmail(String vcProfileEmail) {
		this.vcProfileEmail = vcProfileEmail;
	}

	public String getVcProfileName() {
		return vcProfileName;
	}

	public void setVcProfileName(String vcProfileName) {
		this.vcProfileName = vcProfileName;
	}

	public String getVcProfileRolL1() {
		return vcProfileRolL1;
	}

	public void setVcProfileRolL1(String vcProfileRolL1) {
		this.vcProfileRolL1 = vcProfileRolL1;
	}

	public String getVcProfileRolL1extendido() {
		return vcProfileRolL1extendido;
	}

	public void setVcProfileRolL1extendido(String vcProfileRolL1extendido) {
		this.vcProfileRolL1extendido = vcProfileRolL1extendido;
	}

	public String getVcProfileRolL2EM() {
		return vcProfileRolL2EM;
	}

	public void setVcProfileRolL2EM(String vcProfileRolL2EM) {
		this.vcProfileRolL2EM = vcProfileRolL2EM;
	}

	public String getVcProfileRolL2AR() {
		return vcProfileRolL2AR;
	}

	public void setVcProfileRolL2AR(String vcProfileRolL2AR) {
		this.vcProfileRolL2AR = vcProfileRolL2AR;
	}

	public String getVcProfileRolL2AN() {
		return vcProfileRolL2AN;
	}

	public void setVcProfileRolL2AN(String vcProfileRolL2AN) {
		this.vcProfileRolL2AN = vcProfileRolL2AN;
	}

	public String getVcProfileRolL2SE() {
		return vcProfileRolL2SE;
	}

	public void setVcProfileRolL2SE(String vcProfileRolL2SE) {
		this.vcProfileRolL2SE = vcProfileRolL2SE;
	}

	public String getVcProfileRolL3() {
		return vcProfileRolL3;
	}

	public void setVcProfileRolL3(String vcProfileRolL3) {
		this.vcProfileRolL3 = vcProfileRolL3;
	}

	public String getVcProfileRolL4() {
		return vcProfileRolL4;
	}

	public void setVcProfileRolL4(String vcProfileRolL4) {
		this.vcProfileRolL4 = vcProfileRolL4;
	}

	public String getVcProfileRolExperienceEM() {
		return vcProfileRolExperienceEM;
	}

	public void setVcProfileRolExperienceEM(String vcProfileRolExperienceEM) {
		this.vcProfileRolExperienceEM = vcProfileRolExperienceEM;
	}

	public String getVcProfileRolExperienceAR() {
		return vcProfileRolExperienceAR;
	}

	public void setVcProfileRolExperienceAR(String vcProfileRolExperienceAR) {
		this.vcProfileRolExperienceAR = vcProfileRolExperienceAR;
	}

	public String getVcProfileSkillCloudNativeExperience() {
		return vcProfileSkillCloudNativeExperience;
	}

	public void setVcProfileSkillCloudNativeExperience(String vcProfileSkillCloudNativeExperience) {
		this.vcProfileSkillCloudNativeExperience = vcProfileSkillCloudNativeExperience;
	}

	public String getVcProfileSkillLowCodeExperience() {
		return vcProfileSkillLowCodeExperience;
	}

	public void setVcProfileSkillLowCodeExperience(String vcProfileSkillLowCodeExperience) {
		this.vcProfileSkillLowCodeExperience = vcProfileSkillLowCodeExperience;
	}

	public String getVcProfileSectorExperience() {
		return vcProfileSectorExperience;
	}

	public void setVcProfileSectorExperience(String vcProfileSectorExperience) {
		this.vcProfileSectorExperience = vcProfileSectorExperience;
	}

	public String getVcProfileSkillCloudExp() {
		return vcProfileSkillCloudExp;
	}

	public void setVcProfileSkillCloudExp(String vcProfileSkillCloudExp) {
		this.vcProfileSkillCloudExp = vcProfileSkillCloudExp;
	}

	@Override
	public int compareTo(FormDataImport o) {
		// TODO Auto-generated method stub
		return Integer.compare(this.getId(), o.getId());
	}

}
