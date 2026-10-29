
package com.example.demo.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidPatchRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidPatch(
            InvalidPatchRequestException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),
                request.getRequestURI(), Map.of("patch", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() == null
                                ? "Invalid value"
                                : fieldError.getDefaultMessage(),
                        (first, second) -> first,
                        LinkedHashMap::new));

        log.warn("Request validation failed for {}",
                request.getRequestURI());

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed. Check the supplied fields.",
                request.getRequestURI(),
                errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidBody(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Request body is missing or contains invalid JSON.",
                request.getRequestURI(),
                Map.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            NoResourceFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, "The requested resource was not found.",
                request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED,
                "The HTTP method is not supported for this resource.",
                request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnsupportedMediaType(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Content type is not supported. Use application/json.",
                request.getRequestURI(), Map.of());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Required request parameter is missing: "
                        + ex.getParameterName(),
                request.getRequestURI(),
                Map.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid value supplied for parameter: "
                        + ex.getName(),
                request.getRequestURI(),
                Map.of());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "One or more supplied values violate validation rules.",
                request.getRequestURI(),
                Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        log.warn("Database integrity violation for {}",
                request.getRequestURI());

        return buildResponse(
                HttpStatus.CONFLICT,
                "The request conflicts with existing database data.",
                request.getRequestURI(),
                Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error while processing {}",
                request.getRequestURI(), ex);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected server error occurred.",
                request.getRequestURI(),
                Map.of());
    }

    private ResponseEntity<ApiResponse<Void>> buildResponse(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> validationErrors) {

        ApiResponse<Void> response = new ApiResponse<>(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                null,
                validationErrors);

        return ResponseEntity.status(status).body(response);
    }
}