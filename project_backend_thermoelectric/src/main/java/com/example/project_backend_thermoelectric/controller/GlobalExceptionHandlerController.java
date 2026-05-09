package com.example.project_backend_thermoelectric.controller;

import com.example.project_backend_thermoelectric.exception.DuplicateResourceException;
import com.example.project_backend_thermoelectric.exception.NotFoundResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlerController {
    @ExceptionHandler(NotFoundResourceException.class)
    public ResponseEntity<?> handleNotFound(NotFoundResourceException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateResourceException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}
