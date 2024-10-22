package com.ccsw.capabilitymanager.versionstaffing;

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

import com.ccsw.capabilitymanager.fileprocess.model.DataserviceS3;
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

	/**
	 * Retrieves a file from MinIO storage based on the provided ID and optional file name.
	 * 
	 * <p>This method fetches a file from MinIO by first retrieving the file metadata from the database using the provided ID.
	 * If the file metadata is found, the file name is obtained from the metadata and used to fetch the file from MinIO.</p>
	 * 
	 * @param id The ID of the file to be retrieved. This ID is used to locate the file metadata in the database.
	 * @param fileName The name of the file to be retrieved. If not provided or null, the file name is retrieved from the
	 *                 database record associated with the provided ID.
	 * 
	 * @return An {@link InputStream} for the file retrieved from MinIO.
	 * 
	 * @throws IOException If an I/O error occurs while accessing the file from MinIO.
	 * @throws MinioException If an error occurs while interacting with the MinIO server.
	 * @throws InvalidKeyException If the key used to access MinIO is invalid.
	 * @throws NoSuchAlgorithmException If the algorithm used for accessing MinIO is not found.
	 * @throws IllegalArgumentException If an illegal argument is provided to the MinIO file retrieval method.
	 * @throws java.security.InvalidKeyException If the key used to access MinIO is invalid in a security context.
	 * 
	 * <p>If the file metadata cannot be found in the database, an error is logged and an exception is thrown. The error
	 * logging includes details about the method name, status, error message, and stack trace.</p>
	 */
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
	
	/**
	 * Logs detailed error information for debugging and monitoring purposes.
	 * 
	 * @param function The name of the method or function where the error occurred.
	 * @param status The HTTP status code associated with the error.
	 * @param errorMessage A specific error message describing the nature of the error.
	 * @param message Additional context or details about the error.
	 * @param trace Stack trace or additional information useful for debugging.
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
