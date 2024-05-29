package com.ccsw.capabilitymanager.itinerariosdataimport.model;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "itinerarios")
public class ItinerariosDataImport  implements Comparable<ItinerariosDataImport>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "GGID", nullable = false)
	private String GGID;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email_id")
	private String email;
	
	@Column(name = "global_grade")
	private String globalGrade;
	
	@Column(name = "country")
	private String country;

	@Column(name = "SBU", nullable = false)
	private String sbu;

	@Column(name = "BU")
	private String bu;

	@Column(name = "pathway_id")
	private String pathwayId;

	@Column(name = "pathway_title")
	private String pathwayTitle;
	
	@Column(name = "total_pathway_content")
	private Integer totalPathwayContent;

	@Column(name = "completed_content")
	private Integer completedContent;
	
	@Column(name = "completion_percent")
	private String completionPercent;

	@Column(name = "enrollment_date")
	private Date enrollmentDate;
	
	@Column(name = "recent_activity_date")
	private Date recentActivityDate;

	@Column(name = "completed_date")
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

	public String getPathwayId() {
		return pathwayId;
	}

	public void setPathwayId(String pathwayId) {
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

	@Override
	public int compareTo(ItinerariosDataImport o) {
		
		return 0;
	}






	
}
