package com.ccsw.capabilitymanager.dataimport.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ImportResponseDto {

	private LocalDateTime timestamp;
	private HttpStatus status;
	private String message;
	private String error;
	private String trace;
	private String path;
	private String bucketName;

	public ImportResponseDto() {
		this.timestamp = LocalDateTime.now();
		this.status = HttpStatus.ACCEPTED;
		this.message = "";
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
