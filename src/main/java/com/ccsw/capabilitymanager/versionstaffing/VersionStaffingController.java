package com.ccsw.capabilitymanager.versionstaffing;

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


@RequestMapping(value = "/version-staffing")
@RestController
public class VersionStaffingController {
    @Autowired
    private VersionStaffingService versionStaffingService;

    /**
     * Handles a GET request to download a file from MinIO storage.
     * 
     * <p>This endpoint retrieves a file based on the provided ID and optional file name. The file is fetched from
     * MinIO using the `versionStaffingService` to access the file by its ID. The file is then returned as a 
     * downloadable resource with the appropriate headers set for content disposition.</p>
     * 
     * @param id The ID of the file to be retrieved. This ID is used to locate the file in the database.
     * @param fileName The name of the file to be retrieved. This is an optional parameter; if not provided, 
     *                 it may be determined based on the file information associated with the given ID.
     * 
     * @return A {@link ResponseEntity} containing the file as a {@link Resource}. The response is set to 
     *         `HttpStatus.OK` and the content type is set to `MediaType.APPLICATION_OCTET_STREAM`. The `Content-Disposition`
     *         header is set to indicate that the response should be treated as a file attachment with the provided filename.
     * 
     * @throws InvalidKeyException If the key used to access MinIO is invalid.
     * @throws java.security.InvalidKeyException If the key used to access MinIO is invalid in a security context.
     * @throws NoSuchAlgorithmException If the algorithm used for accessing MinIO is not found.
     * @throws IllegalArgumentException If an illegal argument is provided to the MinIO file retrieval method.
     * @throws IOException If an I/O error occurs while retrieving the file from MinIO.
     * @throws MinioException If an error occurs while interacting with the MinIO server.
     * 
     * <p>The method logs debug information about the file retrieval process. If the file is successfully retrieved, it is 
     * returned as a downloadable resource. The filename used in the `Content-Disposition` header is derived from the 
     * `InputStreamResource` and is included in the response to facilitate file download on the client side.</p>
     */
    @GetMapping(path = "/download-file/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id,String fileName) throws InvalidKeyException, java.security.InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException, MinioException{
    	CapabilityLogger.logDebug(" >>>> getFile " + id);
    	
    	InputStream inputStream = (InputStream) versionStaffingService.recoverFileById(id, fileName);  
    	InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
    	
    	CapabilityLogger.logDebug("      getFile >>>>");
    	return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + inputStreamResource.getFilename() + "\"")
				.body(inputStreamResource);
    }    
}
