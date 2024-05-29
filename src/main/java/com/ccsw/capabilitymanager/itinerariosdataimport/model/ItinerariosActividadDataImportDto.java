package com.ccsw.capabilitymanager.itinerariosdataimport.model;

import java.util.Date;

public class ItinerariosActividadDataImportDto {

	private Integer id;
    private String GGID;
    private String firstName;
    private String lastName;
    private String email;
    private String globalGrade;
    private String country;
    private String sbu;
    private String bu;
    private Integer pathwayId;
    private String pathwayTitle;
    private Integer totalPathwayContent;
    private Integer completedContent;
    private String completionPercent;
    private Date enrollmentDate;
    private Date recentActivityDate;
    private Date completedDate;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGGID() {
		return GGID;
	}
	public void setGGID(String gGID) {
		GGID = gGID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGlobalGrade() {
		return globalGrade;
	}
	public void setGlobalGrade(String globalGrade) {
		this.globalGrade = globalGrade;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSbu() {
		return sbu;
	}
	public void setSbu(String sbu) {
		this.sbu = sbu;
	}
	public String getBu() {
		return bu;
	}
	public void setBu(String bu) {
		this.bu = bu;
	}
	public Integer getPathwayId() {
		return pathwayId;
	}
	public void setPathwayId(Integer pathwayId) {
		this.pathwayId = pathwayId;
	}
	public String getPathwayTitle() {
		return pathwayTitle;
	}
	public void setPathwayTitle(String pathwayTitle) {
		this.pathwayTitle = pathwayTitle;
	}
	public Integer getTotalPathwayContent() {
		return totalPathwayContent;
	}
	public void setTotalPathwayContent(Integer totalPathwayContent) {
		this.totalPathwayContent = totalPathwayContent;
	}
	public Integer getCompletedContent() {
		return completedContent;
	}
	public void setCompletedContent(Integer completedContent) {
		this.completedContent = completedContent;
	}
	public String getCompletionPercent() {
		return completionPercent;
	}
	public void setCompletionPercent(String completionPercent) {
		this.completionPercent = completionPercent;
	}
	public Date getEnrollmentDate() {
		return enrollmentDate;
	}
	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}
	public Date getRecentActivityDate() {
		return recentActivityDate;
	}
	public void setRecentActivityDate(Date recentActivityDate) {
		this.recentActivityDate = recentActivityDate;
	}
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
    
    


}
