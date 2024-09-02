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

	/**
	 * Handles exceptions of type {@link MyBadAdviceException} and returns a response with a BAD_REQUEST status.
	 *
	 * <p>This method is invoked when a {@link MyBadAdviceException} is thrown. It returns a response entity with
	 * a message from the exception and an HTTP status of 400 Bad Request.</p>
	 *
	 * @param e The {@link MyBadAdviceException} instance that was thrown.
	 * @return A {@link ResponseEntity} containing the exception message and a BAD_REQUEST HTTP status.
	 */
    @ExceptionHandler
    public ResponseEntity<String> myBadAdviceException(MyBadAdviceException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        // return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Handles exceptions of type {@link MyConflictAdviceException} and returns a response with a CONFLICT status.
     *
     * <p>This method is triggered when a {@link MyConflictAdviceException} is thrown. It returns a response entity with
     * the exception's message and an HTTP status of 409 Conflict.</p>
     *
     * @param e The {@link MyConflictAdviceException} instance that was thrown.
     * @return A {@link ResponseEntity} containing the exception message and a CONFLICT HTTP status.
     */
    @ExceptionHandler
    public ResponseEntity<String> myConflictAdviceException(MyConflictAdviceException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
        // return ResponseEntity.status(409).body(e.getMessage());
    }
    
    /**
     * Handles exceptions of type {@link ImportException} and returns a response with the details of the import operation.
     *
     * <p>This method is invoked when an {@link ImportException} is thrown. It extracts the {@link ImportResponseDto}
     * from the exception and returns it in the response body with an HTTP status of 200 OK.</p>
     *
     * @param ex The {@link ImportException} instance that was thrown, containing the import response details.
     * @return A {@link ResponseEntity} containing the {@link ImportResponseDto} with the import operation details and
     *         an HTTP status of 200 OK.
     */
    @ExceptionHandler
    public ResponseEntity<ImportResponseDto> handleImportException(ImportException ex) {
        ImportResponseDto importResponseDto = ex.getImportResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(importResponseDto);
    }
    
    /**
     * Handles exceptions of type {@link ItinerarioExistenteException} and returns a response with a BAD_REQUEST status.
     *
     * <p>This method is invoked when an {@link ItinerarioExistenteException} is thrown. It creates an {@link ErrorMessage}
     * object with the exception's message and returns it in the response body with an HTTP status of 400 Bad Request.</p>
     *
     * @param ex The {@link ItinerarioExistenteException} instance that was thrown, containing the error details.
     * @return A {@link ResponseEntity} containing the {@link ErrorMessage} with the error details and an HTTP status of 400 Bad Request.
     */
    @ExceptionHandler(ItinerarioExistenteException.class)
    public ResponseEntity<Object> handleItinerarioExistenteException(ItinerarioExistenteException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles exceptions of type {@link ItinerarioVacioException} and returns a response with a BAD_REQUEST status.
     *
     * <p>This method is invoked when an {@link ItinerarioVacioException} is thrown. It creates an {@link ErrorMessage}
     * object with the exception's message and returns it in the response body with an HTTP status of 400 Bad Request.</p>
     *
     * @param ex The {@link ItinerarioVacioException} instance that was thrown, containing the error details.
     * @return A {@link ResponseEntity} containing the {@link ErrorMessage} with the error details and an HTTP status of 400 Bad Request.
     */
    @ExceptionHandler(ItinerarioVacioException.class)
    public ResponseEntity<Object> handleItinerarioVacioException(ItinerarioVacioException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions of type {@link ResponseStatusException} and returns a response with the appropriate status.
     *
     * <p>This method is invoked when a {@link ResponseStatusException} is thrown. It returns a response entity with
     * the reason provided in the exception and the HTTP status associated with it.</p>
     *
     * @param ex The {@link ResponseStatusException} instance that was thrown, containing the reason and status.
     * @return A {@link ResponseEntity} containing the reason from the exception and the HTTP status specified in the exception.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ImportResponseDto> handleStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(ex.getReason(), ex.getStatus());
    }

}
