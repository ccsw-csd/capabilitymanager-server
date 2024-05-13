package com.ccsw.capabilitymanager.S3Service.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.minio.MinioClient;

@Component
public class DataserviceS3 {

	
	@Value("${s3.endpoint}")
	private String s3Endpoint;

	@Value("${s3.username}")
	private String username;

	@Value("${s3.password}")
	private String password;

	@Value("${s3.bucket}")
	private String bucketName;
	
	private MinioClient minioClient;
	
	
	public String getS3Endpoint() {
		return s3Endpoint;
	}

	public void setS3Endpoint(String s3Endpoint) {
		this.s3Endpoint = s3Endpoint;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	public MinioClient getMinioClient() {

		if (minioClient != null)
			return minioClient;

		minioClient = MinioClient.builder().endpoint(s3Endpoint).credentials(username,password).build();

		return minioClient;
	}

	
}
