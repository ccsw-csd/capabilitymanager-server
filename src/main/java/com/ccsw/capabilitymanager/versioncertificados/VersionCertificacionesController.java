package com.ccsw.capabilitymanager.versioncertificados;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.ccsw.capabilitymanager.common.logs.CapabilityLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.minio.errors.MinioException;


@RequestMapping(value = "/version-certificaciones")
@RestController
public class VersionCertificacionesController {
    @Autowired
    private VersionCertificacionesService versionCertificacionesService;
    
    /**
     * Handles the file download request by retrieving the file from the service and returning it as a downloadable resource.
     * 
     * @param id The ID of the file to retrieve.
     * @param fileName The name of the file to retrieve.
     * @return A {@link ResponseEntity} containing the file as a {@link Resource} for download.
     * 
     * @throws InvalidKeyException If the key used for accessing the file is invalid.
     * @throws NoSuchAlgorithmException If the algorithm required for accessing the file is not found.
     * @throws IllegalArgumentException If an illegal argument is provided to the file retrieval method.
     * @throws IOException If an I/O error occurs while retrieving or processing the file.
     * @throws MinioException If an error occurs while interacting with the MinIO server.
     * 
     * <p>This method processes a GET request to download a file. It retrieves the file using the provided 
     * ID and file name from the {@link VersionCertificacionesService}. The file is then wrapped in an 
     * {@link InputStreamResource} and returned in the HTTP response with an `application/octet-stream` 
     * content type, allowing the file to be downloaded by the client. The `Content-Disposition` header is 
     * set to indicate that the file should be treated as an attachment with the given file name.</p>
     * 
     * <p>The method also logs debugging information before and after file retrieval for tracking purposes.</p>
     */
    @GetMapping(path = "/download-file/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id, String fileName) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException, MinioException{
    	CapabilityLogger.logDebug(" >>>> getFile " + id);

    	InputStream inputStream = (InputStream) versionCertificacionesService.recoverFileById(id,fileName);
    	InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
		CapabilityLogger.logDebug("      getFile >>>>");
    	return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + inputStreamResource.getFilename() + "\"")
				.body(inputStreamResource);
    }    
}
