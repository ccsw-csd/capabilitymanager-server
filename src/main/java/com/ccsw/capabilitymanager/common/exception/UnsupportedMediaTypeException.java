package com.ccsw.capabilitymanager.common.exception;

import org.springframework.http.HttpStatus;
import com.ccsw.capabilitymanager.dataimport.model.ImportResponseDto;

/**
 * UnsupportedMediaTypeException: error con código 415 UNSUPPORTED MEDIA TYPE,
 * para envíos que no contienen ficheros válidos o procesables.
 */
@SuppressWarnings("serial")
public class UnsupportedMediaTypeException extends ResponseStatusException {

    /**
     * Constructor: crear ResponseStatusException con código 415 y Objeto con el error.
     */
    public UnsupportedMediaTypeException(ImportResponseDto reason) {
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, reason);
    }
    
    
    /**
     * Constructor: crear ResponseStatusException con código 415 y Objeto con el error.
     */
    public UnsupportedMediaTypeException(String reasonString) {
        super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, reasonString);
    }

}
