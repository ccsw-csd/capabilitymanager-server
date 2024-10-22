package com.ccsw.capabilitymanager.fileprocess;

import com.ccsw.capabilitymanager.fileprocess.dto.FileProcessDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;

public interface S3Service {
	
	void uploadFile(ImportRequestDto dto);
	
	boolean uploadFile(FileProcessDto dto);
}
