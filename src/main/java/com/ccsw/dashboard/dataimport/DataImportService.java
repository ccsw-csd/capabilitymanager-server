package com.ccsw.dashboard.dataimport;

import com.ccsw.dashboard.dataimport.model.ImportRequestDto;
import com.ccsw.dashboard.dataimport.model.ImportResponseDto;

public interface DataImportService {

	ImportResponseDto processObject(ImportRequestDto dto);

}
