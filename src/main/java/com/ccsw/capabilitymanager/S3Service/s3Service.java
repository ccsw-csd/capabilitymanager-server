package com.ccsw.capabilitymanager.S3Service;

import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;

public interface s3Service {
	
	void uploadFile(ImportRequestDto dto);
	

}
