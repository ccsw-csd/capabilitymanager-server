package com.ccsw.capabilitymanager.versionstaffing;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.S3Service.model.DataserviceS3;
import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.versionstaffing.model.VersionStaffing;

import io.jsonwebtoken.security.InvalidKeyException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VersionStaffingServiceImpl implements VersionStaffingService {
	private static final Logger logger = LoggerFactory.getLogger(VersionStaffingServiceImpl.class);
	private static final String ERROR_INIT = ">>> [ERROR][VersionStaffingServiceImpl] (";
	@Autowired
	private VersionStaffingRepository versionStaffingRepository;

	@Autowired
	private DataserviceS3 dataservice;


	@Override
	public InputStream recoverFileById(Long id, String fileName) throws IOException, MinioException,
			InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, java.security.InvalidKeyException {

		MinioClient minioClient = dataservice.getMinioClient();
		Optional<VersionStaffing> opStaffingDataImportFile = versionStaffingRepository.findById(id);
		VersionStaffing versionStaffing = null;
		if (opStaffingDataImportFile.isPresent()) {
			versionStaffing = opStaffingDataImportFile.get();
			fileName = versionStaffing.getNombreFichero();

		} else {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), HttpStatus.NOT_FOUND,
					Constants.ERROR_FILE_NOT_FOUND, Constants.ERROR_FILE_NOT_FOUND, null);
		}
		return minioClient.getObject(GetObjectArgs.builder().bucket(dataservice.getBucketName()).object(fileName).build());
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
