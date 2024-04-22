package com.ccsw.dashboard.versioncapacidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.S3Service.s3Service;
import com.ccsw.dashboard.common.Constants;
import com.ccsw.dashboard.versioncapacidades.model.VersionCapacidades;

import io.jsonwebtoken.security.InvalidKeyException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VersionCapacidadesServiceImpl implements VersionCapacidadesService {
	private static final Logger logger = LoggerFactory.getLogger(VersionCapacidadesServiceImpl.class);
	private static final String ERROR_INIT  = ">>> [ERROR][VersionStaffingServiceImpl] (";
	@Autowired
	private VersionCapatidadesRepository versionCapatidadesRepository;
	
	@Value("${s3.endpoint}")
	private String s3Endpoint;

	@Value("${s3.username}")
	private String username;

	@Value("${s3.password}")
	private String password;

	@Value("${s3.bucket}")
	private String bucketName;

	@Autowired
	s3Service s3service;
	
	private MinioClient minioClient;
	
	@Override
	public InputStream recoverFileById(Long id,String fileName) throws IOException, MinioException, InvalidKeyException,
	NoSuchAlgorithmException, IllegalArgumentException, java.security.InvalidKeyException  {
		Resource resource = null;
		minioClient = s3service.getMinioClient();

		Optional<VersionCapacidades> opStaffingDataImportFile = versionCapatidadesRepository.findById(id);
		VersionCapacidades versionCapacidades = null;
		if (opStaffingDataImportFile.isPresent()) {
			versionCapacidades = opStaffingDataImportFile.get();
			try {
				fileName =  versionCapacidades.getNombreFichero();
				String[] fileNameArr = fileName.split("\\.");
				String extension = versionCapacidades.getNombreFichero().contains(Constants.XLSX_FILE_EXTENSION ) ? Constants.XLSX_FILE_EXTENSION : Constants.XLS_FILE_EXTENSION;

			    File archivoTemporal = File.createTempFile(fileNameArr[0]+"-", extension);
			    
			    try (FileOutputStream fos = new FileOutputStream(archivoTemporal)) {
				    
			        resource = (Resource) new FileSystemResource(archivoTemporal);
			        }
				} catch (IOException e) {
					setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), HttpStatus.NOT_FOUND,
							e.getMessage(), e.getLocalizedMessage(), null);

				}
			} else {
				setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), HttpStatus.NOT_FOUND,
						Constants.ERROR_FILE_NOT_FOUND, Constants.ERROR_FILE_NOT_FOUND, null);
			}
			return minioClient.getObject(GetObjectArgs.builder()
					.bucket(bucketName)
					.object(fileName)
					.build());
	}

	private  void setErrorToReturn( String function, HttpStatus status, String errorMessage , String message, String trace) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(ERROR_INIT).append( function ).append(Constants.ERROR_INIT2);
		logger.error(errorData.toString() + " Status: " + status);
		if(errorMessage != null && !errorMessage.isBlank() && !errorMessage.isEmpty())
			logger.error(errorData.toString() + " ERROR: " + errorMessage);
		if(message != null && !message.isBlank() && !message.isEmpty())
			logger.error(errorData.toString() + " MESSAGE: " + message);
		if(trace != null && !trace.isBlank() && !trace.isEmpty())
			logger.error(errorData.toString() + " TRACE: " + trace);
    }
}
