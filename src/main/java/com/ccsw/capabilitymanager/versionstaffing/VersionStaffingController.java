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
