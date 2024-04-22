package com.ccsw.dashboard.S3Service;

import org.springframework.web.multipart.MultipartFile;

import com.ccsw.dashboard.dataimport.model.ImportRequestDto;

import io.minio.MinioClient;

public interface s3Service {
	
	void uploadFile(ImportRequestDto dto,MultipartFile file);
	MinioClient getMinioClient();

}
