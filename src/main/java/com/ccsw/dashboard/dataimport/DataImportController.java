package com.ccsw.dashboard.dataimport;


import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.dashboard.dataimport.model.ImportRequestDto;
import com.ccsw.dashboard.dataimport.model.ImportResponseDto;

@RequestMapping(value = "/import")
@RestController
public class DataImportController {
	private static final Logger logger = LoggerFactory.getLogger(DataImportController.class);
	
    @Autowired
    private DataImportService formDataImportService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "/data", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<ImportResponseDto> importData(@RequestPart("importRequestDto") @ModelAttribute ImportRequestDto dto){
    	logger.debug(" >>>> importData ");
    	ImportResponseDto importResponseDto = formDataImportService.processObject(dto);
    	logger.debug("      importData >>>>");
    	if (importResponseDto.getError() == null ) {
    		importResponseDto.setMessage("Data imported correctly");
    		return ResponseEntity.status(HttpStatus.OK).body(importResponseDto);
    	} else {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(importResponseDto);
    	}
    }    
}
