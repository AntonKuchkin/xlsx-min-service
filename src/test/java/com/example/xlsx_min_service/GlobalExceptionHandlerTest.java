package com.example.xlsx_min_service;

import com.example.xlsx_min_service.controller.GlobalExceptionHandler;
import com.example.xlsx_min_service.exception.FileProcessingException;
import com.example.xlsx_min_service.exception.InvalidCellFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleFileNotFound() {
        FileProcessingException ex = new FileProcessingException("Test file not found");
        ResponseEntity<String> response = handler.handleFileNotFound(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Test file not found", response.getBody());
    }

    @Test
    void testHandleInvalidCellFormatException() {
        InvalidCellFormatException ex = new InvalidCellFormatException("Invalid cell");
        ResponseEntity<String> response = handler.handleInvalidCellFormatException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid cell", response.getBody());
    }
}

