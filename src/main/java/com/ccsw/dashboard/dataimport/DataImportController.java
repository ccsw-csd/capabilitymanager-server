package com.ccsw.dashboard.dataimport;


import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ccsw.dashboard.dataimport.model.ImportRequestDto;
import com.ccsw.dashboard.dataimport.model.ImportResponseDto;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;


@RequestMapping(value = "/import")
@RestController
public class DataImportController {
	private static final Logger logger = LoggerFactory.getLogger(DataImportController.class);
	
    @Autowired
    private DataImportService formDataImportService;
    
    @Autowired
    DozerBeanMapper mapper;

    @RequestMapping(path = "/data", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<ImportResponseDto> importData(@RequestPart("importRequestDto") @ModelAttribute ImportRequestDto dto,@RequestParam("file") MultipartFile file){
    	logger.debug(" >>>> importData ");
    	ImportResponseDto importResponseDto = null;
		try {
			importResponseDto = formDataImportService.processObject(dto,file);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InsufficientDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	logger.debug("      importData >>>>");
    	
    	
    	
    	if (importResponseDto.getError() == null ) {
    		importResponseDto.setMessage("Data imported correctly");
    		return ResponseEntity.status(HttpStatus.OK).body(importResponseDto);
    	} else {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(importResponseDto);
    	}
    }    
}
