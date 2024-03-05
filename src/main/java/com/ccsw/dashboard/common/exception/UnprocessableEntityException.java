package com.ccsw.dashboard.common.exception;

import org.springframework.http.HttpStatus;

/**
 * UnprocessableEntityException: error con código 422 UNPROCESSABLE ENTITY, para
 * envíos que contienen ficheros válidos, pero con formato o contenido
 * incorrecto.
 */
@SuppressWarnings("serial")
public class UnprocessableEntityException extends ResponseStatusException {

    /**
     * Constructor: crear ResponseStatusException con código 422 y mensaje de error.
     */
    public UnprocessableEntityException(String error) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, error);
    }

}
