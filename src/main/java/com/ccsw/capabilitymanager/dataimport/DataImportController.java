package com.ccsw.capabilitymanager.dataimport;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.ccsw.capabilitymanager.S3Service.s3Service;
import com.ccsw.capabilitymanager.dataimport.model.ImportRequestDto;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;
import com.ccsw.capabilitymanager.exception.ImportException;

@RequestMapping(value = "/import")
@RestController
public class DataImportController {
	private static final Logger logger = LoggerFactory.getLogger(DataImportController.class);

	@Autowired
	private DataImportService formDataImportService;

	@Autowired
	private s3Service s3service;

	@Autowired
	DozerBeanMapper mapper;

	@PostMapping(path = "/data", consumes = { "multipart/form-data" })
	public ResponseEntity<ImportResponseDto> importData(
			@RequestPart("importRequestDto") @ModelAttribute ImportRequestDto dto) {
		logger.debug(" >>>> importData ");
		ImportResponseDto importResponseDto = new ImportResponseDto();
		try {
			// Subir el archivo al bucket de S3
			s3service.uploadFile(dto);
			// Procesar el objeto en la base de datos
			if (importResponseDto.getError() == null) {
				importResponseDto = formDataImportService.processObject(dto);
			}
		} catch (Exception e) {
			// Manejar cualquier excepción que ocurra durante el proceso
			importResponseDto.setError("Error processing file: " + e.getMessage());
			logger.error("Error processing file: " + e.getMessage());
		}
		logger.debug("      importData >>>>");

		if (importResponseDto.getError() == null) {
			importResponseDto.setMessage("Data imported correctly");

			return ResponseEntity.status(HttpStatus.OK).body(importResponseDto);
		} else {
			throw new ImportException(importResponseDto);
		}
	}

}
