package com.ccsw.capabilitymanager.profile.model;

import java.util.List;

import com.ccsw.capabilitymanager.graderole.model.GradeTotal;

public class InformeRoles {

	List<ProfileTotal> architects;
	List<ProfileTotal> softwareEngineer;
	List<ProfileTotal> industryExperts;
	List<ProfileTotal> engagementManagers;
	List<ProfileTotal> businessAnalyst;
	List<ProfileTotal> architectsCustomApps;
	List<ProfileTotal> architectsIntegration;
	List<GradeTotal> gradeTotal;
	
	public List<ProfileTotal> getArchitects() {
		return architects;
	}
	public void setArchitects(List<ProfileTotal> architects) {
		this.architects = architects;
	}
	public List<ProfileTotal> getSoftwareEngineer() {
		return softwareEngineer;
	}
	public void setSoftwareEngineer(List<ProfileTotal> softwareEngineer) {
		this.softwareEngineer = softwareEngineer;
	}
	public List<ProfileTotal> getIndustryExperts() {
		return industryExperts;
	}
	public void setIndustryExperts(List<ProfileTotal> industryExperts) {
		this.industryExperts = industryExperts;
	}
	public List<ProfileTotal> getEngagementManagers() {
		return engagementManagers;
	}
	public void setEngagementManagers(List<ProfileTotal> engagementManagers) {
		this.engagementManagers = engagementManagers;
	}
	public List<ProfileTotal> getBusinessAnalyst() {
		return businessAnalyst;
	}
	public void setBusinessAnalyst(List<ProfileTotal> businessAnalyst) {
		this.businessAnalyst = businessAnalyst;
	}
	public List<ProfileTotal> getArchitectsCustomApps() {
		return architectsCustomApps;
	}
	public void setArchitectsCustomApps(List<ProfileTotal> architectsCustomApps) {
		this.architectsCustomApps = architectsCustomApps;
	}
	public List<ProfileTotal> getArchitectsIntegration() {
		return architectsIntegration;
	}
	public void setArchitectsIntegration(List<ProfileTotal> architectsIntegration) {
		this.architectsIntegration = architectsIntegration;
	}
	public List<GradeTotal> getGradeTotal() {
		return gradeTotal;
	}
	public void setGradeTotal(List<GradeTotal> gradeTotal) {
		this.gradeTotal = gradeTotal;
	}

	
	
	
}
