package com.ccsw.capabilitymanager.versioncapacidades;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.S3Service.model.DataserviceS3;
import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.versioncapacidades.model.VersionCapacidades;

import io.jsonwebtoken.security.InvalidKeyException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VersionCapacidadesServiceImpl implements VersionCapacidadesService {
	private static final String ERROR_INIT = ">>> [ERROR][VersionStaffingServiceImpl] (";
	
	@Autowired
	private VersionCapatidadesRepository versionCapatidadesRepository;

	@Autowired
	private DataserviceS3 dataservice;

	/**
	 * Retrieves a file from MinIO storage based on the provided ID and file name.
	 * 
	 * @param id The ID of the file to retrieve.
	 * @param fileName The name of the file to retrieve.
	 * @return An {@link InputStream} for the file retrieved from MinIO.
	 * 
	 * @throws IOException If an I/O error occurs while retrieving the file.
	 * @throws MinioException If an error occurs while interacting with the MinIO server.
	 * @throws InvalidKeyException If the key used for accessing MinIO is invalid.
	 * @throws NoSuchAlgorithmException If the algorithm required for the file retrieval is not found.
	 * @throws IllegalArgumentException If an illegal argument is provided to the file retrieval method.
	 * @throws java.security.InvalidKeyException If the key used for accessing MinIO is invalid (security-specific).
	 * 
	 * <p>This method attempts to retrieve a file from MinIO storage based on the provided ID and file name. 
	 * It first checks if the file exists in the local database using the provided ID. If the file is found, 
	 * it uses the file name from the database record to retrieve the file from MinIO. If the file is not found 
	 * in the database, it sets an error indicating that the file was not found.</p>
	 * 
	 * <p>The method returns an {@link InputStream} for the requested file if it exists. Otherwise, it sets 
	 * an appropriate error response.</p>
	 */
	@Override
	public InputStream recoverFileById(Long id, String fileName) throws IOException, MinioException,
			InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, java.security.InvalidKeyException {

		MinioClient minioClient = dataservice.getMinioClient();

		Optional<VersionCapacidades> opStaffingDataImportFile = versionCapatidadesRepository.findById(id);
		VersionCapacidades versionCapacidades = null;
		if (opStaffingDataImportFile.isPresent()) {
			versionCapacidades = opStaffingDataImportFile.get();
			fileName = versionCapacidades.getNombreFichero();

		} else {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), HttpStatus.NOT_FOUND,
					Constants.ERROR_FILE_NOT_FOUND, Constants.ERROR_FILE_NOT_FOUND, null);
		}
		return minioClient.getObject(GetObjectArgs.builder().bucket(dataservice.getBucketName()).object(fileName).build());
	}

	/**
	 * Logs an error message and sets an appropriate error response.
	 * 
	 * @param function The name of the function where the error occurred.
	 * @param status The HTTP status to be used in the error response.
	 * @param errorMessage A detailed error message to be logged.
	 * @param message A message to be included in the error log.
	 * @param trace A stack trace or additional details to be included in the error log.
	 * 
	 * <p>This method constructs a detailed error message and logs it using {@link CapabilityLogger}. 
	 * It includes information about the function where the error occurred, the HTTP status, and any 
	 * additional error details or stack traces provided.</p>
	 */
	private void setErrorToReturn(String function, HttpStatus status, String errorMessage, String message,
			String trace) {
		StringBuilder errorData = new StringBuilder();
		errorData.append(ERROR_INIT).append(function).append(Constants.ERROR_INIT2);
		CapabilityLogger.logError(errorData.toString() + " Status: " + status);
		if (errorMessage != null && !errorMessage.isBlank() && !errorMessage.isEmpty())
			CapabilityLogger.logError(errorData.toString() + " ERROR: " + errorMessage);
		if (message != null && !message.isBlank() && !message.isEmpty())
			CapabilityLogger.logError(errorData.toString() + " MESSAGE: " + message);
		if (trace != null && !trace.isBlank() && !trace.isEmpty())
			CapabilityLogger.logError(errorData.toString() + " TRACE: " + trace);
	}
}
