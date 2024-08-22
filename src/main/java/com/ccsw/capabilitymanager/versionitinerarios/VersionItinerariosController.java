package com.ccsw.capabilitymanager.versionitinerarios;

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


@RequestMapping(value = "/version-itinerarios")
@RestController
public class VersionItinerariosController {
    @Autowired
    private VersionItinerariosService versionItinerariosService;
    

    /**
     * Handles the retrieval and download of a file from MinIO storage.
     * 
     * @param id The ID of the file to retrieve.
     * @param fileName The name of the file to retrieve. This parameter is optional and can be null if the filename is 
     *                 retrieved from the database based on the provided ID.
     * @return A {@link ResponseEntity} containing the file as a {@link Resource} for download.
     * 
     * @throws InvalidKeyException If the key used for accessing the MinIO bucket is invalid.
     * @throws NoSuchAlgorithmException If the algorithm used for accessing the MinIO bucket is not found.
     * @throws IllegalArgumentException If an illegal argument is provided to the file retrieval method.
     * @throws IOException If an I/O error occurs while retrieving the file from MinIO.
     * @throws MinioException If an error occurs while interacting with the MinIO server.
     * 
     * <p>This method handles HTTP GET requests to download a file. It uses the provided ID to retrieve the file 
     * details from the service. The file name can be provided directly or retrieved from the database based on the 
     * file ID. The method then uses MinIO client to fetch the file and returns it as a downloadable {@link Resource}. 
     * If any errors occur during file retrieval or while interacting with MinIO, they are thrown as appropriate 
     * exceptions.</p>
     * 
     */
    @GetMapping(path = "/download-file/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id, String fileName) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException, MinioException{
    	CapabilityLogger.logDebug(" >>>> getFile " + id);

    	InputStream inputStream = (InputStream) versionItinerariosService.recoverFileById(id,fileName);
    	InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
		CapabilityLogger.logDebug("      getFile >>>>");
    	return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + inputStreamResource.getFilename() + "\"")
				.body(inputStreamResource);
    }    
}
