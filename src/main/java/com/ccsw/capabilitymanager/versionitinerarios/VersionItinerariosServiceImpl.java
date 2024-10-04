package com.ccsw.capabilitymanager.versionitinerarios;

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
import com.ccsw.capabilitymanager.versionitinerarios.model.VersionItinerarios;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class VersionItinerariosServiceImpl implements VersionItinerariosService {
	private static final String ERROR_INIT = ">>> [ERROR][VersionItinerariosServiceImpl] (";
	@Autowired
	private VersionItinerariosRepository versionItinerariosRepository;

	@Autowired
	private DataserviceS3 dataservice;

	/**
	 * Retrieves a file from MinIO storage based on the given ID. The file name is determined by the 
	 * `VersionItinerarios` entity associated with the provided ID. If the entity is not found, an error is logged 
	 * and a custom exception is thrown.
	 * 
	 * @param id The ID of the file to retrieve.
	 * @param fileName The name of the file to retrieve. This parameter is overridden by the file name
	 *                 obtained from the `VersionItinerarios` entity if the entity is found.
	 * @return An {@link InputStream} representing the file retrieved from MinIO.
	 * 
	 * @throws IOException If an I/O error occurs while retrieving the file from MinIO.
	 * @throws MinioException If an error occurs while interacting with the MinIO server.
	 * @throws InvalidKeyException If the key used for accessing the MinIO bucket is invalid.
	 * @throws NoSuchAlgorithmException If the algorithm used for accessing the MinIO bucket is not found.
	 * @throws IllegalArgumentException If an illegal argument is provided to the file retrieval method.
	 * @throws java.security.InvalidKeyException If the key used for accessing the MinIO bucket is invalid in a security context.
	 * 
	 * <p>This method first checks if the `VersionItinerarios` entity with the specified ID exists. If found, it 
	 * retrieves the file name from this entity and uses the MinIO client to obtain the file from the specified bucket. 
	 * If the entity is not found, an error is logged, and a custom exception is thrown. The method then returns an 
	 * {@link InputStream} for the file, which can be used to read the file content.</p>
	 */
	@Override
	public InputStream recoverFileById(Long id, String fileName) throws IOException, MinioException,
			InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, java.security.InvalidKeyException {

		MinioClient minioClient = dataservice.getMinioClient();
		Optional<VersionItinerarios> opItinerarioDataImportFile = versionItinerariosRepository.findById(id);
		VersionItinerarios versionItinerarios = null;
		if (opItinerarioDataImportFile.isPresent()) {
			versionItinerarios = opItinerarioDataImportFile.get();
			fileName = versionItinerarios.getNombreFichero();
		} else {
			setErrorToReturn(Thread.currentThread().getStackTrace()[1].getMethodName(), HttpStatus.NOT_FOUND,
					Constants.ERROR_FILE_NOT_FOUND, Constants.ERROR_FILE_NOT_FOUND, null);
		}
		return minioClient.getObject(GetObjectArgs.builder().bucket(dataservice.getBucketName()).object(fileName).build());
	}

	/**
	 * Logs an error with detailed information, including the function name, status, and error messages.
	 * 
	 * @param function The name of the function where the error occurred.
	 * @param status The HTTP status code representing the error.
	 * @param errorMessage A detailed error message describing what went wrong.
	 * @param message An optional additional message providing more context about the error.
	 * @param trace An optional stack trace or other diagnostic information.
	 * 
	 * <p>This method constructs an error message using the provided information and logs it using the 
	 * {@link CapabilityLogger}. The message includes details about the function, HTTP status, and any other 
	 * error-specific information provided. This helps in diagnosing and debugging issues in the application.</p>
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
