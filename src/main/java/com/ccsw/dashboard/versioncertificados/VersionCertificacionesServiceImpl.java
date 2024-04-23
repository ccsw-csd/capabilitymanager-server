package com.ccsw.dashboard.versioncertificados;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.S3Service.s3Service;
import com.ccsw.dashboard.common.Constants;
import com.ccsw.dashboard.versioncertificados.model.VersionCertificaciones;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VersionCertificacionesServiceImpl implements VersionCertificacionesService {
	private static final Logger logger = LoggerFactory.getLogger(VersionCertificacionesServiceImpl.class);
	private static final String ERROR_INIT = ">>> [ERROR][VersionCertificacionesServiceImpl] (";
	@Autowired
	private VersionCertificacionesRepository versionCertificacionesRepository;

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

	MinioClient minioClient;

	@Override
	public InputStream recoverFileById(Long id, String fileName) throws IOException, MinioException,
			InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, java.security.InvalidKeyException {

		minioClient = s3service.getMinioClient();
		Optional<VersionCertificaciones> opStaffingDataImportFile = versionCertificacionesRepository.findById(id);
		VersionCertificaciones versionCertificaciones = null;
		if (opStaffingDataImportFile.isPresent()) {
			versionCertificaciones = opStaffingDataImportFile.get();
			fileName = versionCertificaciones.getNombreFichero();
		} else {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), HttpStatus.NOT_FOUND,
					Constants.ERROR_FILE_NOT_FOUND, Constants.ERROR_FILE_NOT_FOUND, null);
		}
		return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
	}

	private void setErrorToReturn(String function, HttpStatus status, String errorMessage, String message,
			String trace) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(ERROR_INIT).append(function).append(Constants.ERROR_INIT2);
		logger.error(errorData.toString() + " Status: " + status);
		if (errorMessage != null && !errorMessage.isBlank() && !errorMessage.isEmpty())
			logger.error(errorData.toString() + " ERROR: " + errorMessage);
		if (message != null && !message.isBlank() && !message.isEmpty())
			logger.error(errorData.toString() + " MESSAGE: " + message);
		if (trace != null && !trace.isBlank() && !trace.isEmpty())
			logger.error(errorData.toString() + " TRACE: " + trace);
	}
}
