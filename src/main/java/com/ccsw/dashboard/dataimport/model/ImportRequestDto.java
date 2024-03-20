package com.ccsw.dashboard.dataimport.model;

import org.springframework.web.multipart.MultipartFile;

public class ImportRequestDto {

	private String documentType;
	// 1- Staffing
	// 2- Roles
	// 3- Certificates
	private String user;
	private String description;
	private MultipartFile fileData;
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public MultipartFile getFileData() {
		return fileData;
	}
	public void setFileData(MultipartFile fileData) {
		this.fileData = fileData;
	}	
}
