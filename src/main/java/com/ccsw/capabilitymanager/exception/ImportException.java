package com.ccsw.capabilitymanager.exception;

import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;


public class ImportException extends RuntimeException {
	
	   private ImportResponseDto importResponseDto;

	    public ImportException(ImportResponseDto importResponseDto) {
	        super();
	        this.importResponseDto = importResponseDto;
	    }

	    public ImportResponseDto getImportResponseDto() {
	        return importResponseDto;
	    }
	}
