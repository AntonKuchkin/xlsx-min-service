package com.example.xlsx_min_service.controller;

import com.example.xlsx_min_service.exception.FileProcessingException;
import com.example.xlsx_min_service.exception.FileReadingException;
import com.example.xlsx_min_service.exception.InvalidCellFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleFileNotFound(FileProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(FileReadingException.class)
    public ResponseEntity<String> handleFileReadingException(FileReadingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCellFormatException.class)
    public ResponseEntity<String> handleInvalidCellFormatException(InvalidCellFormatException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
