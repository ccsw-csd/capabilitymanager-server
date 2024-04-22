package com.ccsw.dashboard.versionstaffing;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.security.InvalidKeyException;
import io.minio.errors.MinioException;


@RequestMapping(value = "/version-staffing")
@RestController
public class VersionStaffingController {
	private static final Logger logger = LoggerFactory.getLogger(VersionStaffingController.class);
	
    @Autowired
    private VersionStaffingService versionStaffingService;

    @RequestMapping(path = "/download-file/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFile(@PathVariable Long id,String fileName) throws InvalidKeyException, java.security.InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException, MinioException{
    	logger.debug(" >>>> getFile " + id);
    	
    	InputStream inputStream = (InputStream) versionStaffingService.recoverFileById(id, fileName);  
    	InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
    	
    	logger.debug("      getFile >>>>");
    	if (inputStreamResource != null ) {
    		return ResponseEntity.status(HttpStatus.OK)
    				.contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + inputStreamResource.getFilename() + "\"")
    				.body(inputStreamResource);
    	}
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }    
}
