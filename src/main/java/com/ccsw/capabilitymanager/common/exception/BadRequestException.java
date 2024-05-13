package com.ccsw.capabilitymanager.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * BadRequestException: error con código 400 BAD REQUEST, para envíos
 * incorrectos.
 */
@SuppressWarnings("serial")
public class BadRequestException extends ResponseStatusException {

    /**
     * Constructor: crear ResponseStatusException con código 400 y mensaje de error.
     */
    public BadRequestException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

}
