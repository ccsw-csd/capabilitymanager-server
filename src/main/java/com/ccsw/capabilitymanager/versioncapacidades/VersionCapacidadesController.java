package com.ccsw.capabilitymanager.versioncapacidades;

import java.io.IOException;
import java.io.InputStream;
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

import io.jsonwebtoken.security.InvalidKeyException;
import io.minio.errors.MinioException;


@RequestMapping(value = "/version-role")
@RestController
public class VersionCapacidadesController {
    @Autowired
    private VersionCapacidadesService versionCapacidadesService;

    /**
     * Downloads a file from the server based on the provided ID and file name.
     * 
     * @param id The ID of the file to download.
     * @param fileName The name of the file to download.
     * @return A {@link ResponseEntity} containing the file as a {@link Resource}, with HTTP status OK.
     * 
     * <p>This method retrieves a file from the server using the provided ID and file name. It streams the file
     * to the client in the response. The response has a content type of application/octet-stream and includes
     * a Content-Disposition header to prompt a file download with the provided file name.</p>
     * 
     * @throws InvalidKeyException If the key used for accessing the file is invalid.
     * @throws java.security.InvalidKeyException If the key used for accessing the file is invalid (security-specific).
     * @throws NoSuchAlgorithmException If the algorithm required for accessing the file is not found.
     * @throws IllegalArgumentException If an illegal argument is provided to the file retrieval method.
     * @throws IOException If an I/O error occurs while retrieving the file.
     * @throws MinioException If an error occurs while interacting with the MinIO server.
     */
    @GetMapping(path = "/download-file/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id,String fileName) throws InvalidKeyException, java.security.InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException, MinioException{
    	CapabilityLogger.logDebug(" >>>> getFile " + id);
    	InputStream inputStream = (InputStream) versionCapacidadesService.recoverFileById(id, fileName);  
    	InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
		CapabilityLogger.logDebug("      getFile >>>>");
    	return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + inputStreamResource.getFilename() + "\"")
				.body(inputStreamResource);
    }    
}
