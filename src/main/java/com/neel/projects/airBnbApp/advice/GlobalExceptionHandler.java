package com.neel.projects.airBnbApp.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.neel.projects.airBnbApp.exception.ResourceNotFoundException;
import com.neel.projects.airBnbApp.exception.UnAuthorisedException;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResponseNotFoundException(Exception e) {
        ApiError error = ApiError.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
                return buildErrorResponseEntity(error);
    }

    @ExceptionHandler(UnAuthorisedException.class)
    public ResponseEntity<ApiResponse<?>> handleResponseUnauthorisedException(Exception e) {
        ApiError error = ApiError.builder()
                .message(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED)
                .build();
                return buildErrorResponseEntity(error);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(Exception e) {
    ApiError error = ApiError.builder()
            .message(e.getMessage())
            .status(HttpStatus.UNAUTHORIZED)
            .build();
        return buildErrorResponseEntity(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(Exception e) {
        ApiError error = ApiError.builder()
                .message(e.getMessage())
                .status(HttpStatus.FORBIDDEN)
                .build();
                return buildErrorResponseEntity(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception e){
        ApiError error = ApiError.builder()
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
            return buildErrorResponseEntity(error);
    }


    public ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(ApiError apiError){
        return new ResponseEntity<>(new ApiResponse<>(apiError), apiError.getStatus());
    }
}
