package com.ccsw.dashboard.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.S3Service.model.DataserviceS3;
import com.ccsw.dashboard.dataimport.model.ImportRequestDto;
import com.ccsw.dashboard.dataimport.model.ImportResponseDto;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class s3ServiceImpl implements s3Service {

	@Autowired
	private DataserviceS3 dataservice;

	public void uploadFile(ImportRequestDto dto) {

		ImportResponseDto importResponseDto = new ImportResponseDto();
		MinioClient minioClient = dataservice.getMinioClient();
		try {
			// Verificar si el bucket existe, si no, crearlo
			boolean found = minioClient
					.bucketExists(BucketExistsArgs.builder().bucket(dataservice.getBucketName()).build());
			if (!found) {
				System.out.println("my-bucketname does not exist");
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(dataservice.getBucketName()).build());
			}
			// Subir el archivo al bucket de S3
			String fileName = dto.getFileData().getOriginalFilename();
			minioClient.putObject(PutObjectArgs.builder().bucket(dataservice.getBucketName()).object(fileName)
					.stream(dto.getFileData().getInputStream(), dto.getFileData().getSize(), -1).build());

			importResponseDto.setMessage("File uploaded to S3 successfully");
		} catch (Exception e) {
			importResponseDto.setError("Error uploading file to S3: " + e.getMessage());
		}
	}
}
