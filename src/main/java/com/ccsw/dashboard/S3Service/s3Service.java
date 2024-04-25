package com.ccsw.dashboard.S3Service;

import com.ccsw.dashboard.dataimport.model.ImportRequestDto;

public interface s3Service {
	
	void uploadFile(ImportRequestDto dto);
	

}
