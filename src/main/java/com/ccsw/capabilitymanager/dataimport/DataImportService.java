package com.ccsw.capabilitymanager.dataimport;

import com.ccsw.capabilitymanager.activitydataimport.model.ActivityDataImportDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;

public interface DataImportService {

	ImportResponseDto processObject(ImportRequestDto dto);


}
