package com.ccsw.capabilitymanager.versioncertificados;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.fileprocess.model.DataserviceS3;
import com.ccsw.capabilitymanager.common.Constants;
import com.ccsw.capabilitymanager.versioncertificados.model.VersionCertificaciones;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VersionCertificacionesServiceImpl implements VersionCertificacionesService {
	private static final String ERROR_INIT = ">>> [ERROR][VersionCertificacionesServiceImpl] (";
	@Autowired
	private VersionCertificacionesRepository versionCertificacionesRepository;

	@Autowired
	private DataserviceS3 dataservice;

	/**
	 * Recovers a file from MinIO storage based on the provided ID and file name.
	 * 
	 * @param id The ID of the file to recover.
	 * @param fileName The name of the file to recover. If the file name is not provided, it will be retrieved from the database.
	 * @return An {@link InputStream} to the file's content.
	 * 
	 * @throws IOException If an I/O error occurs while retrieving the file.
	 * @throws MinioException If an error occurs while interacting with the MinIO server.
	 * @throws InvalidKeyException If the key used for accessing the file is invalid.
	 * @throws NoSuchAlgorithmException If the algorithm required for accessing the file is not found.
	 * @throws IllegalArgumentException If an illegal argument is provided to the file retrieval method.
	 * @throws java.security.InvalidKeyException If the security key used is invalid.
	 * 
	 * <p>This method retrieves a file from MinIO using its ID and file name. It first checks if the file 
	 * exists in the database by querying {@link versionCertificacionesRepository}. If the file exists, the 
	 * method retrieves its name from the database record. It then uses the MinIO client to get the object from 
	 * the specified bucket and returns an {@link InputStream} for the file content. If the file is not found 
	 * in the database, an error is logged and a {@link MinioException} is thrown.</p>
	 */
	@Override
	public InputStream recoverFileById(Long id, String fileName) throws IOException, MinioException,
			InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, java.security.InvalidKeyException {

		MinioClient minioClient = dataservice.getMinioClient();
		Optional<VersionCertificaciones> opStaffingDataImportFile = versionCertificacionesRepository.findById(id);
		VersionCertificaciones versionCertificaciones = null;
		if (opStaffingDataImportFile.isPresent()) {
			versionCertificaciones = opStaffingDataImportFile.get();
			fileName = versionCertificaciones.getNombreFichero();
		} else {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), HttpStatus.NOT_FOUND,
					Constants.ERROR_FILE_NOT_FOUND, Constants.ERROR_FILE_NOT_FOUND, null);
		}
		return minioClient.getObject(GetObjectArgs.builder().bucket(dataservice.getBucketName()).object(fileName).build());
	}

	/**
	 * Logs error details and sets the appropriate error response.
	 * 
	 * @param function The name of the function where the error occurred.
	 * @param status The HTTP status to return with the error response.
	 * @param errorMessage A message describing the error.
	 * @param message Additional information about the error.
	 * @param trace The stack trace or additional trace information.
	 * 
	 * <p>This method constructs and logs detailed error messages for debugging purposes. It uses the provided 
	 * parameters to format and record the error information, including the function name, HTTP status, error 
	 * message, and any additional trace information. This helps in diagnosing issues and provides clarity on 
	 * where and why an error occurred.</p>
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
