package com.ccsw.capabilitymanager.dataimport;

import com.ccsw.capabilitymanager.activity.model.ActivityDTO;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;

public interface DataImportService {

	ImportResponseDto processObject(ImportRequestDto dto);

	void saveActividad(ActivityDTO activityDto);

}
