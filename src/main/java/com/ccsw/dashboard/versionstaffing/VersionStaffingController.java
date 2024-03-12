package com.ccsw.dashboard.versionstaffing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = "/version-staffing")
@RestController
public class VersionStaffingController {
	private static final Logger logger = LoggerFactory.getLogger(VersionStaffingController.class);
	
    @Autowired
    private VersionStaffingService versionStaffingService;

    @RequestMapping(path = "/download-file/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFile(@PathVariable Long id){
    	logger.debug(" >>>> getFile " + id);
    	Resource resource = (Resource) versionStaffingService.recoverFileById(id);
    	logger.debug("      getFile >>>>");
    	if (resource != null ) {
    		return ResponseEntity.status(HttpStatus.OK)
    				.contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
    				.body(resource);
    	}
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }    
}
