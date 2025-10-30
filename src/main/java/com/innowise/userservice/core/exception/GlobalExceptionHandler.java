package com.innowise.userservice.core.exception;

import com.innowise.userservice.api.dto.GetErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GetErrorDto> handleResourceNotFoundException(
        ResourceNotFoundException ex, WebRequest request) {

        return buildErrorResponse(
            ex,
            ex.getMessage(),
            HttpStatus.NOT_FOUND,
            request
        );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<GetErrorDto> handleResourceAlreadyExistsException(
        ResourceAlreadyExistsException ex, WebRequest request) {

        return buildErrorResponse(
            ex,
            ex.getMessage(),
            HttpStatus.CONFLICT,
            request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GetErrorDto> handleDataIntegrityViolation(
        DataIntegrityViolationException ex, WebRequest request) {

        String message = "Database conflict: " + ex.getMostSpecificCause().getMessage();

        logger.warn("Data integrity violation: {}", message);

        return buildErrorResponse(
            ex,
            "A resource with these details already exists or violates data constraints.", // Безопасное сообщение
            HttpStatus.CONFLICT,
            request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GetErrorDto> handleValidationExceptions(
        MethodArgumentNotValidException ex, WebRequest request) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining("; "));

        return buildErrorResponse(
            ex,
            errorMessage,
            HttpStatus.BAD_REQUEST,
            request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GetErrorDto> handleMalformedJson(
        HttpMessageNotReadableException ex, WebRequest request) {

        return buildErrorResponse(
            ex,
            "Invalid request body: JSON is malformed.",
            HttpStatus.BAD_REQUEST,
            request
        );
    }

    //ResourceAlreadyExistsException

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GetErrorDto> handleGlobalException(
        Exception ex, WebRequest request) {

        logger.error("Unhandled exception caught: {}", ex.getMessage(), ex);

        return buildErrorResponse(
            ex,
            "An unexpected internal server error occurred.",
            HttpStatus.INTERNAL_SERVER_ERROR,
            request
        );
    }

    private ResponseEntity<GetErrorDto> buildErrorResponse(
        Exception ex, String message, HttpStatus status, WebRequest request) {

        GetErrorDto errorDto = new GetErrorDto(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorDto, status);
    }
}