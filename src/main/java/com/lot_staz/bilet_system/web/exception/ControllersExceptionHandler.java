package com.lot_staz.bilet_system.web.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllersExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponseObject> handleDataNotFoundException (Exception ex){
        return getErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseObject> handleValidationException (Exception ex){
        return getErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponseObject> handleEntityExistsException (Exception ex){
        return getErrorResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseObject> handleIllegalArgumentException (Exception ex){
        return getErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SeatAlreadyTakenException.class)
    public ResponseEntity<ErrorResponseObject> handleSetAlreadyTakenException(Exception ex){
        return getErrorResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseObject> handleException (Exception ex){
        return getErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponseObject> getErrorResponse(Exception ex, HttpStatus status){
        ErrorResponseObject error = new ErrorResponseObject(ex.getMessage(), status.value());
        return new ResponseEntity<>(error, status);
    }
}
