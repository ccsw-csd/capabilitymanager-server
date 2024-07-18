package com.ccsw.capabilitymanager.exception;

import com.ccsw.capabilitymanager.common.exception.ResponseStatusException;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;

@RestControllerAdvice
public class DefaultHandlerAdviceException extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> myBadAdviceException(MyBadAdviceException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        // return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> myConflictAdviceException(MyConflictAdviceException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
        // return ResponseEntity.status(409).body(e.getMessage());
    }
    
    @ExceptionHandler
    public ResponseEntity<ImportResponseDto> handleImportException(ImportException ex) {
        ImportResponseDto importResponseDto = ex.getImportResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(importResponseDto);
    }
    
    @ExceptionHandler(ItinerarioExistenteException.class)
    public ResponseEntity<Object> handleItinerarioExistenteException(ItinerarioExistenteException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ImportResponseDto> handleStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatus());
    }

}
