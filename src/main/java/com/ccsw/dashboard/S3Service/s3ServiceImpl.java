package com.ccsw.dashboard.S3Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ccsw.dashboard.dataimport.model.ImportRequestDto;
import com.ccsw.dashboard.dataimport.model.ImportResponseDto;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;


@Service
public class s3ServiceImpl implements s3Service {

	@Value("${s3.endpoint}")
	private String s3Endpoint;

	@Value("${s3.username}")
	private String username;

	@Value("${s3.password}")
	private String password;

	@Value("${s3.bucket}")
	private String bucketName;


    private MinioClient minioClient;
    
	  public MinioClient getMinioClient() {

	        if (minioClient != null)
	            return minioClient;

	        minioClient = MinioClient.builder().endpoint(s3Endpoint).credentials(username, password).build();

	        return minioClient;
	    }
	
	
	public void uploadFile(ImportRequestDto dto,MultipartFile file) {
      
		ImportResponseDto importResponseDto = new ImportResponseDto();
	
		minioClient= getMinioClient();
      try {
  		           
          // Verificar si el bucket existe, si no, crearlo
          boolean found = minioClient
          				.bucketExists(BucketExistsArgs
          				.builder()
          				.bucket(bucketName)
          				.build());
          
          System.out.println("Bucket exists: " + found);
          
          if (!found) {
          	System.out.println("my-bucketname does not exist");
              minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
              
          } else {
         	   System.out.println("my-bucketname exists");
       	 }

          // Subir el archivo al bucket de S3
          String fileName = file.getOriginalFilename();
          minioClient.putObject(PutObjectArgs.builder()
                  .bucket(bucketName)
                  .object(fileName)
                  .stream(file.getInputStream(), file.getSize(), -1)
                  .build());
        
          importResponseDto.setMessage("File uploaded to S3 successfully");
      } catch (Exception e) {
          // Manejar cualquier excepción que ocurra durante la carga del archivo a S3
          importResponseDto.setError("Error uploading file to S3: " + e.getMessage());
       //  logger.debug(e.getMessage());
         
      }

  }
}
