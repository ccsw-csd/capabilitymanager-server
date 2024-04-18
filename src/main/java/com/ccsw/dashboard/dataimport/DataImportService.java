package com.ccsw.dashboard.dataimport;

import org.springframework.web.multipart.MultipartFile;

import com.ccsw.dashboard.dataimport.model.Asset;
import com.ccsw.dashboard.dataimport.model.ImportRequestDto;
import com.ccsw.dashboard.dataimport.model.ImportResponseDto;

public interface DataImportService {

	ImportResponseDto processObject(ImportRequestDto dto) ;
	
	void uploadFile(ImportRequestDto dto,MultipartFile file);

	//InputStream getObject(String bucketName, String objectName) throws Exception;

	Asset getObject(String key);


	

	 

}
