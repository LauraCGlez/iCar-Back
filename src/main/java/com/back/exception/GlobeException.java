package com.back.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobeException {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail> userExceptionHandler(UserException ex, WebRequest request) {

        ErrorDetail error = new ErrorDetail(ex.getMessage(), request.getDescription(false), LocalDateTime.now());

        return new ResponseEntity<ErrorDetail>(error, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(DriverException.class)
    public ResponseEntity<ErrorDetail> driverExceptionHandler(DriverException ex, WebRequest request) {

        ErrorDetail error = new ErrorDetail(ex.getMessage(), request.getDescription(false), LocalDateTime.now());

        return new ResponseEntity<ErrorDetail>(error, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(RideException.class)
    public ResponseEntity<ErrorDetail> rideExceptionHandler(RideException ex, WebRequest request) {

        ErrorDetail error = new ErrorDetail(ex.getMessage(), request.getDescription(false), LocalDateTime.now());

        return new ResponseEntity<ErrorDetail>(error, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetail> handlerValidationException(ConstraintViolationException ex) {

        StringBuilder errorMessages = new StringBuilder();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessages.append(violation.getMessage() + "\n");
        }

        ErrorDetail error = new ErrorDetail(errorMessages.toString(), "Validation error", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {

        ErrorDetail error = new ErrorDetail(ex.getBindingResult().getFieldError().getDefaultMessage(), request.getDescription(false), LocalDateTime.now());

        return new ResponseEntity<ErrorDetail>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetail> dataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {

        String message = ex.getRootCause().getMessage();

        String duplicateValue = "";

        String[] tokens = message.split("");

        duplicateValue = tokens[2] + "Already exists";

        System.out.println("-------" + message);

        ErrorDetail error = new ErrorDetail(duplicateValue, "Encounter Duplicated", LocalDateTime.now());

        return new ResponseEntity<ErrorDetail>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> otherExceptionHandler(Exception ex, WebRequest request) {

        ErrorDetail error = new ErrorDetail(ex.getMessage(), request.getDescription(false), LocalDateTime.now());

        return new ResponseEntity<ErrorDetail>(error, HttpStatus.ACCEPTED);
    }

}
